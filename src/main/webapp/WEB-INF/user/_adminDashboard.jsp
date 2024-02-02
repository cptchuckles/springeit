      <h4>Admin Dashboard: All users</h4>
      <table class="table mb-5">
        <thead>
          <tr>
            <th width="20%">Name</th>
            <th width="20%">Date Joined</th>
            <th width="10%">Cringe</th>
            <th width="30%">Email</th>
            <th width="10%">Whines Made</th>
            <th width="10%">Whines Got</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="oneUser" items="${allUsers}">
          <tr>
            <td><strong><a href="/users/${oneUser.id}"><c:out value="${oneUser.username}" /></a></strong></td>
            <td><fmt:formatDate value="${oneUser.createdAt}" pattern="d MMM, yyyy" /></td>
            <td><c:out value="${oneUser.getTotalCringe()}" /></td>
            <td><c:out value="${oneUser.email}" /></td>
            <td><c:out value="${oneUser.whines.size()}" /></td>
            <td><c:out value="${oneUser.getTotalComplaints()}" /></td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
