<div class="card p-3 my-2 d-flex flex-row">
  <h2 class="mx-3" style="height: fit-content; margin: auto"><c:out value="${oneCringe.totalRating()}" /></h2>
  <div class="col">
    <h3><a href="/cringe/${oneCringe.id}"><c:out value="${oneCringe.headline}" /></a></h3>
    <p class="m-0"><fmt:formatDate value="${oneCringe.createdAt}" pattern="d MMM, yyyy" /></p>
  </div>
  <p style="margin: auto"><a href="/users/${oneCringe.user.id}"><c:out value="${oneCringe.user.username}" /></a></p>
</div>
