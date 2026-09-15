let currentUser = null;

function showRegister() {
    document.getElementById('loginBox').style.display = 'none';
    document.getElementById('registerBox').style.display = 'block';
}

// ---------- REGISTER ----------
async function register() {
    const name = document.getElementById('regName').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;

    await fetch('/api/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password })
    });

    alert('Account created! Please login.');
    document.getElementById('registerBox').style.display = 'none';
    document.getElementById('loginBox').style.display = 'block';
}

// ---------- LOGIN ----------
async function login() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    if (!email || !password) {
        alert('Please enter both email and password');
        return;
    }

    try {
        const res = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        const data = await res.json();

        if (data.error) {
            alert(data.error);
            return;
        }

        currentUser = data;
        document.getElementById('loginBox').style.display = 'none';
        document.getElementById('appBox').style.display = 'block';
        document.getElementById('welcomeText').textContent = `Welcome, ${currentUser.name} (${currentUser.role})`;

        loadBooks();
        loadMyBooks();
        loadMyBookings();

        if (currentUser.role === 'ADMIN') {
            document.getElementById('adminSection').style.display = 'block';
            loadManageBooks();
            loadIssuedBooks();
            loadMembers();
            loadFines();
            loadAdminMessages();
        }
    } catch (err) {
        alert('Login failed. Is the server running?');
        console.error(err);
    }
}

function logout() {
    currentUser = null;
    location.reload();
}

// ---------- CATALOGUE (all users see this) ----------
async function loadBooks() {
    const search = document.getElementById('searchBox').value;
    const res = await fetch('/api/books?search=' + encodeURIComponent(search));
    const books = await res.json();

    const container = document.getElementById('bookList');
    container.innerHTML = '';

    books.forEach(book => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `
            <strong>${book.title}</strong> by ${book.author}<br>
            Category: ${book.category} | Available: ${book.availableQuantity}/${book.quantity}
        `;

        const actionBtn = document.createElement('button');
        if (book.availableQuantity > 0) {
            actionBtn.textContent = 'Issue Book';
            actionBtn.onclick = () => issueBook(book.id);
        } else {
            actionBtn.textContent = 'Advance Book (currently out)';
            actionBtn.onclick = () => advanceBook(book.id);
        }
        card.appendChild(actionBtn);

        container.appendChild(card);
    });
}

async function issueBook(bookId) {
    const res = await fetch(`/api/books/${bookId}/issue/${currentUser.id}`, { method: 'POST' });
    const result = await res.json();
    alert(typeof result === 'string' ? result : 'Book issued! Due in 14 days.');
    loadBooks();
    loadMyBooks();
}

async function advanceBook(bookId) {
    await fetch(`/api/books/${bookId}/book/${currentUser.id}`, { method: 'POST' });
    alert('Advance booking placed. You will be notified when it is free.');
    loadMyBookings();
}

// ---------- MY BOOKS (user) ----------
async function loadMyBooks() {
    const res = await fetch(`/api/users/${currentUser.id}/my-books`);
    const records = await res.json();

    const container = document.getElementById('myBooks');
    container.innerHTML = '';

    if (records.length === 0) {
        container.textContent = 'No books issued right now.';
        return;
    }

    records.forEach(record => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `
            <strong>${record.book.title}</strong><br>
            Due date: ${record.dueDate}
        `;
        const returnBtn = document.createElement('button');
        returnBtn.textContent = 'Return Book';
        returnBtn.onclick = () => returnBook(record.id);
        card.appendChild(returnBtn);
        container.appendChild(card);
    });
}

async function returnBook(issueId) {
    const res = await fetch(`/api/issues/${issueId}/return`, { method: 'POST' });
    const result = await res.json();
    alert(result.fine > 0 ? `Returned. Fine: ₹${result.fine}` : 'Returned. No fine.');
    loadBooks();
    loadMyBooks();
}

// ---------- MY ADVANCE BOOKINGS (user) ----------
async function loadMyBookings() {
    const res = await fetch(`/api/users/${currentUser.id}/bookings`);
    const bookings = await res.json();

    const container = document.getElementById('myBookingsList');
    container.innerHTML = '';

    if (bookings.length === 0) {
        container.textContent = 'No advance bookings.';
        return;
    }

    bookings.forEach(b => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `<strong>${b.book.title}</strong><br>Booked on: ${b.bookingDate}`;
        container.appendChild(card);
    });
}

// ---------- CONTACT FORM (user) ----------
async function sendMessage() {
    const message = document.getElementById('contactMsg').value;
    if (!message.trim()) return;

    await fetch('/api/contact', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: currentUser.name, message })
    });

    alert('Message sent to admin!');
    document.getElementById('contactMsg').value = '';
}

// ===================== ADMIN ONLY BELOW =====================

// ---------- MANAGE BOOKS: list with Edit / Delete ----------
async function loadManageBooks() {
    const res = await fetch('/api/books');
    const books = await res.json();

    const container = document.getElementById('manageBookList');
    container.innerHTML = '';

    books.forEach(book => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `
            <strong>${book.title}</strong> by ${book.author} (${book.category})<br>
            ISBN: ${book.isbn} | Qty: ${book.quantity}
        `;

        const editBtn = document.createElement('button');
        editBtn.textContent = 'Edit';
        editBtn.onclick = () => editBook(book);
        card.appendChild(editBtn);

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Delete';
        deleteBtn.onclick = () => deleteBook(book.id);
        card.appendChild(deleteBtn);

        container.appendChild(card);
    });
}

async function editBook(book) {
    const title = prompt('Title:', book.title);
    if (title === null) return; // user hit cancel
    const author = prompt('Author:', book.author);
    const isbn = prompt('ISBN:', book.isbn);
    const category = prompt('Category:', book.category);
    const quantity = prompt('Quantity:', book.quantity);

    await fetch(`/api/admin/books/${book.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, author, isbn, category, quantity: parseInt(quantity) })
    });

    alert('Book updated!');
    loadManageBooks();
    loadBooks();
}

async function deleteBook(bookId) {
    if (!confirm('Delete this book?')) return;

    await fetch(`/api/admin/books/${bookId}`, { method: 'DELETE' });
    alert('Book deleted!');
    loadManageBooks();
    loadBooks();
}

async function addBook() {
    const book = {
        title: document.getElementById('newTitle').value,
        author: document.getElementById('newAuthor').value,
        isbn: document.getElementById('newIsbn').value,
        category: document.getElementById('newCategory').value,
        quantity: parseInt(document.getElementById('newQty').value)
    };

    await fetch('/api/admin/books', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(book)
    });

    alert('Book added!');
    loadManageBooks();
    loadBooks();
}

// ---------- VIEW ALL ISSUED BOOKS ----------
async function loadIssuedBooks() {
    const res = await fetch('/api/admin/issued-books');
    const records = await res.json();

    const container = document.getElementById('issuedBooksList');
    container.innerHTML = '';

    if (records.length === 0) {
        container.textContent = 'No books currently issued.';
        return;
    }

    records.forEach(r => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `
            <strong>${r.book.title}</strong> issued to ${r.user.name}<br>
            Due date: ${r.dueDate}
        `;
        container.appendChild(card);
    });
}

// ---------- VIEW MEMBERS ----------
async function loadMembers() {
    const res = await fetch('/api/admin/members');
    const members = await res.json();

    const container = document.getElementById('membersList');
    container.innerHTML = '';

    members.forEach(m => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `<strong>${m.name}</strong> (${m.role}) - ${m.email}`;
        container.appendChild(card);
    });
}

// ---------- FINE MANAGEMENT ----------
async function loadFines() {
    const res = await fetch('/api/admin/issued-books');
    const records = await res.json();

    const container = document.getElementById('finesList');
    container.innerHTML = '';

    const withFines = records.filter(r => r.fine > 0);

    if (withFines.length === 0) {
        container.textContent = 'No pending fines.';
        return;
    }

    withFines.forEach(r => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `${r.user.name} owes ₹${r.fine} for "${r.book.title}"`;

        const payBtn = document.createElement('button');
        payBtn.textContent = 'Mark Paid';
        payBtn.onclick = () => markFinePaid(r.id);
        card.appendChild(payBtn);

        container.appendChild(card);
    });
}

async function markFinePaid(issueId) {
    await fetch(`/api/admin/fines/${issueId}/mark-paid`, { method: 'PUT' });
    alert('Fine marked as paid!');
    loadFines();
}

// ---------- VIEW CONTACT MESSAGES ----------
async function loadAdminMessages() {
    const res = await fetch('/api/admin/messages');
    const messages = await res.json();

    const container = document.getElementById('adminMessagesList');
    container.innerHTML = '';

    if (messages.length === 0) {
        container.textContent = 'No messages yet.';
        return;
    }

    messages.forEach(m => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `<strong>${m.name}</strong> (${m.sentDate}): ${m.message}`;
        container.appendChild(card);
    });
}