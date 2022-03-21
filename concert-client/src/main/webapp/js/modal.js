export const Modal = {

    displayAlertBox(container, title, message, onOk) {

        // Add modal box
        const modal = document.createElement("div");
        modal.classList.add("modal");
        modal.innerHTML =
            `<div class="modal-content">
                <span class="close-modal">&times;</span>
                <div class="flex-col align-center">
                    <h1>${title}</h1>
                    <p class="description">${message}</p>
                    <button>Ok!</button>
                </div>
            </div>`;
        container.appendChild(modal);
        this.enableModal(modal, null, null, true);
        modal.querySelector("button").addEventListener("click", modal.closeModal);
        modal.afterModalClose = onOk;
        modal.openModal();
    },

    enableModalsOnPage() {

        // Get the modals
        const modals = document.querySelectorAll(".modal");

        // For each one, hook it up.
        modals.forEach(modal => this.enableModal(modal));
    },

    enableModal(modal, overrideOpenButton = null, overrideCloseButton = null, removeOnClose = false) {

        // Get the button which will show the modal
        let openButton;
        if (overrideOpenButton) {
            openButton = overrideOpenButton;
        }
        else {
            const buttonId = modal.getAttribute("data-openButtonId");
            openButton = document.querySelector(buttonId);
        }

        // Get the element which closes the modal (if any)
        let closeButton;
        if (overrideCloseButton) {
            closeButton = overrideCloseButton;
        }
        else {
            closeButton = modal.querySelector(".close-modal");
        }

        // Get info about what kind of modal this is
        let mode = modal.getAttribute("data-modalMode");
        if (!mode) {
            mode = "dismissable";
        }

        // When the user clicks the open button, open the modal
        if (openButton) {
            openButton.addEventListener("click", openModal.bind(modal));
        }

        // When the user clicks "close", close it
        if (closeButton) {
            closeButton.addEventListener("click", closeModal.bind(modal));
        }

        // Give the modal itself access to open and close programmatically.
        modal.openModal = openModal.bind(modal);
        modal.closeModal = closeModal.bind(modal);

        function openModal() {

            const event = {
                target: this,
                cancel: false
            };

            if (this.beforeModalOpen) {
                this.beforeModalOpen(event);
            }

            if (!event.cancel) {

                // console.log(this);
                this.style.display = "block";

                if (mode === "dismissable") {
                    window.addEventListener("click", windowCloseModal);
                }

                if (this.afterModalOpen) {
                    this.afterModalOpen(event);
                }
            }
        }

        function closeModal() {

            const event = {
                target: this,
                cancel: false
            };

            if (this.beforeModalClose) {
                this.beforeModalClose(event);
            }

            if (!event.cancel) {
                // console.log(this);
                this.style.display = "none";

                if (mode === "dismissable") {
                    window.removeEventListener("click", windowCloseModal);
                }

                if (this.afterModalClose) {
                    this.afterModalClose(event);
                }

                if (removeOnClose) {
                    this.remove();
                }
            }
        }

        function windowCloseModal(event) {
            if (event.target === modal) {
                closeModal.bind(modal)();
            }
        }

    }

};