<div class="card p-3 d-flex flex-row">
  <div class="col">
    <a href="/cringe/${oneCringe.id}"><h3><c:out value="${oneCringe.headline}" /></h3></a>
    <p>Posted by <c:out value="${oneCringe.user.username}" /></p>
    <p>on <c:out value="${oneCringe.createdAt}" /></p>
  </div>
  <div class="h-100 d-flex flex-col jusityf-content-between">
    <h2><c:out value="${oneCringe.totalRating()}" /></h2>
  </div>
</div>
