<div class="card p-3 d-flex flex-row">
  <div class="col">
    <h3><a href="/cringe/${oneCringe.id}"><c:out value="${oneCringe.headline}" /></a></h3>
    <p>
      Posted by <a href="/users/${oneCringe.user.id}"><c:out value="${oneCringe.user.username}" /></a><br />
      <small>on <c:out value="${oneCringe.createdAt}" /></small>
    </p>
  </div>
  <div class="h-100 d-flex flex-col jusityf-content-between">
    <h2><c:out value="${oneCringe.totalRating()}" /></h2>
  </div>
</div>
