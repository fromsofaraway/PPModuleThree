// вообще не сам придумал, нашел в комментариях, показалось прагматичным и красивым создать объект по типу сервиса
// для переиспользования далее в функциях
const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch('http://localhost:8080/api/users'),
    findOneUser: async (id) => await fetch(`http://localhost:8080/api/users/${id}`),
    addNewUser: async (user) => await fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`http://localhost:8080/api/users/${id}`, {
        method: 'PATCH',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`http://localhost:8080/api/users/${id}`, {
        method: 'DELETE',
        headers: userFetchService.head
    })
}


async function fillTableWithUsers() {
    await userFetchService.findAllUsers()
        .then(response => response.json())
        .then(data => {
            // ссылка на объект в хтмл
            const tableBody = document.querySelector('#users-table tbody');
            // чистим данные в теле таблицы, если там что-то есть
            cleanTableWithUsers()

            // в цикле идем по каждому элементу и добавляем строку
            data.forEach(user => {
                // создаем строку
                const row = document.createElement('tr');

                // создаем мапу ролей и вставляем пробел
                const roles = user.roles.map(role => role.name).join(', ');

                // добавляем ячейки для каждого юзера
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.username}</td>
                    <td>${user.age}</td>
                    <td>${roles}</td>
                    <td>
                        <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-outline-secondary"
                        data-toggle="modal" data-target="#editUserModal" 
                        onclick="getEditModal(${user.id})">Edit
                        </button>
                    </td>
                    <td>
                        <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-outline-danger"
                        data-toggle="modal" data-target="#someDefaultModal" onclick="performDelete(${user.id})">Delete</button>
                    </td>
                  `;

                // добавляем всю строку в таблицу
                tableBody.appendChild(row);
            });

        });
}

// получаем данные при первой загрузке страницы
// window.onload(fillTableWithUsers())
document.addEventListener("DOMContentLoaded", fillTableWithUsers);


async function performUserCreate() {
    // ссылка на форму
    const form = document.getElementById('performUserCreate');


    await form.addEventListener('submit', event => {
        event.preventDefault();

        // берем форму из хтмл
        const formData = new FormData(form);
        // создаем соответствие айдишников, как в базе данных
        const roleIds = new Map([
            ['ROLE_USER', 1],
            ['ROLE_ADMIN', 2]
        ]);
        // мапа ролей
        const roles = formData.getAll('role').map((roleName) => ({
            id: roleIds.get(roleName),
            name: roleName
        }));
        // объект юзера, полученный из заполненных данных в форму
        const user = {
            name: formData.get('name'),
            age: formData.get('age'),
            username: formData.get('username'),
            password: formData.get('password'),
            roles: roles
        };

        userFetchService.addNewUser(user)
            // после добавления обновляем данные
            .then(fillTableWithUsers)
            // и ресетим форму
            .then(document.getElementById('performUserCreate').reset())
    });
    // красиво переходим обратно в список юзеров
    document.querySelector('#users-tab').click();
}

async function performDelete(userId) {
    userFetchService.deleteUser(userId)
        .then(fillTableWithUsers)
}

async function getEditModal(userId) {
    // получаем объект юзера
    const user = await userFetchService.findOneUser(userId)
        .then(response => response.json());

    // создаем модалку и помещаем туда полученные данные от юзера
    const modal = document.createElement('div');
    modal.classList.add('modal');
    modal.innerHTML = `
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit User</h5>
                </div>
                <div class="modal-body d-flex justify-content-center border position-sticky p-3 bg-white">
                    <form id="editUserForm">
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input type="text" class="form-control" id="name" value="${user.name}">
                        </div>
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" id="username" value="${user.username}">
                        </div>
                        <div class="form-group">
                            <label for="age">Age</label>
                            <input type="number" class="form-control" id="age" value="${user.age}">
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password" value="${user.password}">
                        </div>
                        <div class="form-group">
                            <label for="role" class="form-label d-flex justify-content-center">Role</label>
                            <div style="text-align: center;">
                                <select id="roles" name="roles" multiple>
                                     <option value="ROLE_USER">ROLE_USER</option>
                                     <option value="ROLE_ADMIN">ROLE_ADMIN</option>
                                </select>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="editFormSaveBtn">Save changes</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="editFormCloseBtn">Close</button>
                </div>
            </div>
        </div>
    `;

    // удаляем модалку, если закрыть окно
    const closeButton = modal.querySelector('#editFormCloseBtn');
    closeButton.addEventListener('click', () => {
        modal.remove();
    });

    // если кликнуть мышкой вне модалки, она закроется
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.remove()
        }
    });

    // кнопка сохранения
    const saveButton = modal.querySelector('#editFormSaveBtn');
    saveButton.addEventListener('click', async () => {
        // создаем переменные для редактированного юзера
        const name = modal.querySelector('#name').value;
        const username = modal.querySelector('#username').value;
        const age = modal.querySelector('#age').value;
        const password = modal.querySelector('#password').value;
        const roles = modal.querySelector('#roles').selectedOptions;

        // создаем соответствие айдишников, как в БД
        const roleNamesToIds = {
            'ROLE_USER': 1,
            'ROLE_ADMIN': 2,
        };

        // создаем новый объект отредактированного юзера
        const updatedUser = {
            name,
            username,
            age,
            password,
            roles: Array.from(roles)
                .map(role => ({id: roleNamesToIds[role.value], name: role.value}))
        };

        // шлем патч запрос и затем обновляем данные таблицы
        await userFetchService.updateUser(updatedUser, userId)
            .then(fillTableWithUsers)

        // удаляем модалку
        modal.remove();
    });


    // суем модалку в тело документа и отображаем ее
    document.body.appendChild(modal);
    modal.style.display = 'block';
}


// функция для очистки данных из тела таблицы, что позволяет обновлять данные при каждом действии без перезагрузки
async function cleanTableWithUsers() {
    const userTable = document.getElementById('users-table-body');

    // есть ли в теле что-нибудь
    const numUsers = userTable.children.length;
    // если в теле что-нибудь есть, чистим
    if (numUsers > 0) {
        userTable.innerHTML = '';
    }
}

async function getLoggedUser() {
    fetch("/auth/logged")
        .then(response => response.json())
        .then(data => {
            // ссылка на span в шапке документа
            const userInfo = document.getElementById('currentUser')
            const roles = data.user.roles.map(role => role.name).join(', ')
            userInfo.innerText = data.user.username + " with roles " + roles
        });

}

document.addEventListener("DOMContentLoaded", getLoggedUser);

async function fillTableWithCurrentUser() {
    async function getUserId() {
        const response = await fetch("auth/logged");
        const data = await response.json();
        return data.user.id;
    }

    const userId = await getUserId()
    console.log(userId)
    await userFetchService.findOneUser(userId)
        .then(response => response.json())
        .then(user => {
            const tableBody = document.querySelector('#current-user-table tbody');

            const row = document.createElement('tr');
            const roles = user.roles.map(role => role.name).join(', ');
            row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.username}</td>
                    <td>${user.age}</td>
                    <td>${roles}</td>
                  `;
            tableBody.appendChild(row);

        });
}

document.addEventListener("DOMContentLoaded", fillTableWithCurrentUser);