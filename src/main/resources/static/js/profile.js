document.addEventListener('DOMContentLoaded', function() {
    // --- Tab navigation ---
    const tabLinks = document.querySelectorAll('.profile-tabs a');
    const tabContents = document.querySelectorAll('.profile-tab-content');

    tabLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            // Remove active class from all tabs and contents
            tabLinks.forEach(tab => tab.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            // Add active class to clicked tab
            this.classList.add('active');

            // Show corresponding content
            const tabId = this.getAttribute('data-tab');
            document.getElementById(tabId + '-content').classList.add('active');
        });
    });

    // --- Farmer Info Edit Toggle ---
    const editFarmerInfoBtn = document.getElementById('editFarmerInfoBtn');
    const farmerInfoDisplay = document.getElementById('farmerInfoDisplay');
    const farmerInfoEdit = document.getElementById('farmerInfoEdit');
    const cancelFarmerInfoBtn = document.getElementById('cancelFarmerInfoBtn');
    const farmerInfoForm = document.getElementById('farmerInfoForm');
    const formNotice = document.getElementById('formNotice');
    const profileImage = document.getElementById('profileImage');
    const profileImageForm = document.getElementById('profileImageForm');

    if (editFarmerInfoBtn && farmerInfoDisplay && farmerInfoEdit && cancelFarmerInfoBtn) {
        editFarmerInfoBtn.addEventListener('click', async function() {
            farmerInfoDisplay.classList.add('hidden');
            farmerInfoEdit.classList.remove('hidden');
        });

        cancelFarmerInfoBtn.addEventListener('click', function() {
            farmerInfoEdit.classList.add('hidden');
            farmerInfoDisplay.classList.remove('hidden');
        });
    }

    if (farmerInfoForm) {
        farmerInfoForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const formData = new FormData();
            formData.append('fullName', document.getElementById("fullName").value);
            formData.append('phone', document.getElementById("phone").value);
            formData.append('experience', document.getElementById("experience").value);
            formData.append('specialization', document.getElementById("specialization").value);

            const id = document.getElementById("id");

            try {
                const method = id && id.value? 'PATCH' : 'POST';

                const response = await fetch('/api/profile', {
                    method,
                    credentials: 'include',
                    body: formData
                });

                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error.message || 'Failed to save profile');
                }

                alert("Profile saved successfully!");
                location.reload();
            } catch (error) {
                formNotice.textContent = 'Error: ' + error.message;
                formNotice.style.color = 'red';
            }
        });
    }

    if (profileImageForm) {
        profileImage.addEventListener('change', async function(e) {
            e.preventDefault();

            const formData = new FormData();
            formData.append('profileImage', document.getElementById("profileImage").value);

            const fileInput = document.getElementById('profileImage');
            if (fileInput.files[0]) {
                formData.append('profileImage', fileInput.files[0]);
            }
            const id = document.getElementById("id");

            try {
                const method = id && id.value? 'PATCH' : 'POST';

                const response = await fetch('/api/profile', {
                    method,
                    credentials: 'include',
                    body: formData
                });

                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error.message || 'Failed to save profile image');
                }

                alert("Profile saved successfully!");
                location.reload();
            } catch (error) {
                formNotice.textContent = 'Error: ' + error.message;
                formNotice.style.color = 'red';
            }
        });
    }

    const closeModalBtns = document.querySelectorAll('.close-modal, .cancel-modal');

    if (closeModalBtns) {
        closeModalBtns.forEach(btn => {
            btn.addEventListener('click', function() {
                document.querySelectorAll('.modal').forEach(modal => {
                    modal.classList.remove('show');
                });
            });
        });
    }

    // --- Profile image upload preview ---
    const profileImageUpload = document.getElementById('profileImageUpload');
    const profileAvatar = document.querySelector('.profile-avatar img');
    const profileAvatarPlaceholder = document.querySelector('.profile-avatar-placeholder');

    if (profileImageUpload && (profileAvatar || profileAvatarPlaceholder)) {
        profileImageUpload.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    if (profileAvatar) {
                        profileAvatar.src = e.target.result;
                    } else if (profileAvatarPlaceholder) {
                        const newImg = document.createElement('img');
                        newImg.src = e.target.result;
                        newImg.alt = 'Profile Picture';
                        profileAvatarPlaceholder.parentNode.replaceChild(newImg, profileAvatarPlaceholder);
                    }
                };
                reader.readAsDataURL(this.files[0]);
            }
        });
    }
});
