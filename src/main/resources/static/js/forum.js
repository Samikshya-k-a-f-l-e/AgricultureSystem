// forum.js
document.addEventListener('DOMContentLoaded', function() {
    // Modal handling (if you have modals)
    const newTopicBtn = document.getElementById('new-topic-btn');
    if (newTopicBtn) {
        newTopicBtn.addEventListener('click', openModal);
    }

    const closeBtn = document.querySelector('.close-modal');
    if (closeBtn) {
        closeBtn.addEventListener('click', closeModal);
    }

    // Form submission for new topics (if exists)
    const topicForm = document.getElementById('newTopicForm');
    if (topicForm) {
        topicForm.addEventListener('submit', handleSubmit);
    }

    // Reply form handling
    const replyForm = document.getElementById('replyForm');
    if (replyForm) {
        replyForm.addEventListener('submit', handleReplySubmit);
    }

    // Date formatting
    document.querySelectorAll(".date").forEach(function(el) {
        const rawDate = el.getAttribute("data-updated-at");
        if (rawDate) {
            el.querySelector("span").innerText = timeAgo(rawDate);
        }
    });
});

function openModal() {
    const modal = document.getElementById('newTopicModal');
    if (modal) modal.style.display = 'block';
}

function closeModal() {
    const modal = document.getElementById('newTopicModal');
    if (modal) modal.style.display = 'none';
}

async function handleSubmit(e) {
    e.preventDefault();

    const idField = document.getElementById("id");
    const isEdit = idField && idField.value;

    const topicData = {
        title: document.getElementById('topicTitle').value.trim(),
        content: document.getElementById('topicContent').value.trim(),
        categoryId: document.getElementById('topicCategory').value,
        tags: document.getElementById('topicTags').value.trim()
    };

    const url = isEdit ? `/api/forum/topics/${idField.value}` : '/api/forum/topics';
    const method = isEdit ? 'PATCH' : 'POST';

    try {
        const response = await fetch(url, {
            method,
            headers: {'Content-Type': 'application/json'},
            credentials: 'include',
            body: JSON.stringify(topicData)
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || 'Failed to save topic');
        }

        if (result.redirectUrl) {
            window.location.href = result.redirectUrl;
        } else {
            location.reload();
        }

    } catch (err) {
        alert("Error: " + err.message);
    }
}

function handleReplySubmit(e) {
    e.preventDefault();

    const replyData = {
        topicId: parseInt(document.getElementById("topicId").value),
        content: document.getElementById("replyContent").value.trim(),
        notifyReplies: document.getElementById("notifyReplies").checked
    };

    if (!replyData.content) {
        alert("Please enter your reply content");
        return;
    }

    fetch('/api/forum/replies', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(replyData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw err; });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "success") {
                document.getElementById("replyContent").value = "";
                window.location.reload();
            } else {
                throw new Error(data.message || "Failed to post reply");
            }
        })
        .catch(error => {
            alert("Error: " + (error.message || "Failed to post reply"));
        });
}

let currentPage = /*[[${currentPage}]]*/ 0;
const currentTopicSlug = window.location.pathname.split('/').pop();
const totalPages = /*[[${repliesPage.totalPages}]]*/ 0;

function loadMoreReplies() {
    currentPage++;
    fetch('/forum/' + currentTopicSlug + '?page=' + currentPage, {
        headers: {
            'X-Requested-With': 'XMLHttpRequest' // Important for server-side detection
        }
    })
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newReplies = doc.querySelectorAll('.post-replies .post');

            if (newReplies.length === 0) {
                throw new Error('No new replies found');
            }

            const repliesContainer = document.querySelector('.post-replies');
            newReplies.forEach(reply => {
                repliesContainer.appendChild(reply.cloneNode(true));
            });

            // Update showing text
            const newShowingText = doc.querySelector('.load-more span');
            if (newShowingText) {
                document.querySelector('.load-more span').textContent = newShowingText.textContent;
            }

            // Hide button if no more pages
            if (currentPage >= totalPages - 1) {
                document.querySelector('.load-more button').style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Error loading more replies:', error);
            currentPage--; // Revert page increment on error
        });
}

function deleteForum(id) {
    if (confirm("Are you sure you want to delete this topic?")) {
        fetch(`/api/forum/topics/${id}`, { method: 'DELETE' })
            .then(res => {
                if (!res.ok) {
                    return res.text().then(text => { throw new Error(text); });
                }
                location.reload();
            })
            .catch(err => alert(err.message));
    }
}

function editForum(id) {
    document.querySelector('#newTopicModal h2').textContent = 'Update Forum Topic';

    fetch(`/api/forum/topics/${id}`)
        .then(response => {
            console.log(`Fetching from: /api/forum/topics/${id}`);
            if (!response.ok) {
                throw new Error('Failed to fetch topic');
            }
            return response.json();
        })
        .then(data => {
            console.log('Parsed data:', data);
            document.getElementById('id').value = data.id;
            document.getElementById('topicTitle').value = data.title;
            document.getElementById('topicCategory').value = data.categoryId;
            document.getElementById('topicContent').value = data.content;
            document.getElementById('topicTags').value = data.tags;

            document.getElementById('newTopicModal').style.display = 'block';
        })
        .catch(err => alert("Failed to load topic: " + err));
}

function deleteReply(id) {
    if (confirm("Are you sure you want to delete this reply?")) {
        fetch(`/api/forum/replies/${id}`, { method: 'DELETE' })
            .then(res => {
                if (!res.ok) {
                    return res.text().then(text => { throw new Error(text); });
                }
                location.reload();
            })
            .catch(err => alert(err.message));
    }
}

async function handleReaction(button) {
    const reactionType = button.getAttribute('data-reaction-type');
    const topicId = button.getAttribute('data-topic-id');
    const replyId = button.getAttribute('data-reply-id');
    const countSpan = button.querySelector(".count");
    const currentCount = parseInt(countSpan.innerText, 10);

    try {
        const response = await fetch('/api/reactions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                reactionType: reactionType,
                topicId: topicId,
                replyId: replyId
            })
        });

        if (!response.ok) throw new Error(await response.text());

        const result = await response.json();

        // Update reaction count based on action
        switch (result.action) {
            case "added":
                countSpan.innerText = currentCount + 1;
                button.classList.add('active');
                // Remove active class from other reaction buttons in the same group
                const siblingButtons = button.parentElement.querySelectorAll('[data-reaction-type]');
                siblingButtons.forEach(sibling => {
                    if (sibling !== button) {
                        sibling.classList.remove('active');
                        const siblingCount = sibling.querySelector(".count");
                        const count = parseInt(siblingCount.innerText, 10);
                        if (count > 0) siblingCount.innerText = count - 1;
                    }
                });
                break;
            case "removed":
                countSpan.innerText = currentCount - 1;
                button.classList.remove('active');
                break;
            case "updated":
                // Remove one from previous reaction and add one to new reaction
                button.classList.add('active');
                const otherButtons = button.parentElement.querySelectorAll('[data-reaction-type]');
                otherButtons.forEach(other => {
                    if (other !== button && other.classList.contains('active')) {
                        other.classList.remove('active');
                        const otherCount = other.querySelector(".count");
                        const count = parseInt(otherCount.innerText, 10);
                        if (count > 0) otherCount.innerText = count - 1;
                    }
                });
                countSpan.innerText = currentCount + 1;
                break;
        }
    } catch (error) {
        console.error("Reaction error:", error);
        alert("Failed to process reaction: " + error.message);
    }
}