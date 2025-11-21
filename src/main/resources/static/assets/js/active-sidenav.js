$(document).ready(function () {
    const currentPath = window.location.pathname;

    $('.sidebar .nav-link').each(function () {
        let linkPath = $(this).attr('href');
        const dataPath = $(this).attr('data-path');

        if (dataPath) {
            linkPath = dataPath;
        }

        if ((linkPath === '#' || linkPath === undefined) && !dataPath) {
            $(this).removeClass('active');
            return;
        }

        if (currentPath.startsWith(linkPath) && linkPath !== '/admin') {
            $(this).addClass('active');
            if ($(this).hasClass('collapsed')) {
                $(this).removeClass('collapsed');
                $(this).next('.collapse').addClass('show');
            }
        } else if (linkPath === '/admin' && currentPath === '/admin') {
            $(this).addClass('active');
        } else {
            $(this).removeClass('active');
        }
    });

    $('.sidebar .collapse-item').each(function () {
        const linkPath = $(this).attr('href');

        if (linkPath && currentPath.startsWith(linkPath)) {
            $(this).addClass('active');
        } else {
            $(this).removeClass('active');
        }
    });
});
