export const Utils = {
    /**
     * Gets the value of the cookie with the given name, or null if there's no such cookie.
     * @param cname the name of the cookie
     * @returns the value of the cookie, or null
     */
    getCookie(cname) {
        const name = `${cname}=`;
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i].trim();
            // while (c.charAt(0) == ' ') {
            //     c = c.substring(1);
            // }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return null;
    },

    /**
     * Removes all child HTML elements from the given element.
     * @param element the element whose children to remove from the DOM
     */
    clear(element) {
        let child = element.firstElementChild;
        while (child) {
            element.removeChild(element.firstElementChild);
            child = element.firstElementChild;
        }
    }
};