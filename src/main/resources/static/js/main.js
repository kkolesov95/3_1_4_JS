$(document).ready(function () {
    $('.table .eBtn').on('click', function (event) {

        event.preventDefault();
        var href = $(this).attr('href');
        $('.editForm #rolesEdit option[value="1"]').attr('selected',false);
        $('.editForm #rolesEdit option[value="2"]').attr('selected',false);

        $.get(href, function (users, status) {
            $('.editForm #idEdit').val(users.id)
            $('.editForm #firstNameEdit').val(users.name)
            $('.editForm #lastNameEdit').val(users.lastname)
            $('.editForm #ageEdit').val(users.age)
            $('.editForm #usernameEdit').val(users.email)

            users.roles.forEach((role) => {
                if (role.role === 'ROLE_USER') {
                    $('.editForm #rolesEdit option[value="1"]').attr('selected',true);
                }
                if (role.role === 'ROLE_ADMIN') {
                    $('.editForm #rolesEdit option[value="2"]').attr('selected',true);
                }
            });
        });

        $('.editForm #editModal').modal();
    });
});

$(document).ready(function () {
    $('.table .dBtn').on('click', function (event) {

        event.preventDefault();
        var href = $(this).attr('href');
        $('.deleteForm #rolesDelete option[value="1"]').attr('selected',false);
        $('.deleteForm #rolesDelete option[value="2"]').attr('selected',false);

        $.get(href, function (users, status) {
            $('.deleteForm #idDelete').val(users.id)
            $('.deleteForm #firstNameDelete').val(users.name)
            $('.deleteForm #lastNameDelete').val(users.lastname)
            $('.deleteForm #ageDelete').val(users.age)
            $('.deleteForm #usernameDelete').val(users.email)

            users.roles.forEach((role) => {
                if (role.role === 'ROLE_USER') {
                    $('.deleteForm #rolesDelete option[value="1"]').attr('selected',true);
                }
                if (role.role === 'ROLE_ADMIN') {
                    $('.deleteForm #rolesDelete option[value="2"]').attr('selected',true);
                }
            });
        });

        $('.deleteForm #deleteModal').modal();
    });
});