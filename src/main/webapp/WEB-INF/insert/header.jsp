<header class="w-100 p-5 border-bottom">
  <div class="container d-flex flex-row justify-content-between align-items-center">
    <a href="/cringe" class="link-dark link-underline-opacity-0"><h3>SpringeIt</h3></a>
    <div class="d-flex flex-row gap-2 align-items-baseline">
      <a href="/users/${currentUser.id}"><strong><c:out value="${currentUser.username}" /></strong></a>
      <a href="/logout" class="btn btn-danger">Logout</a>
    </div>
  </div>
</header>
