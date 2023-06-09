<#import "dashboardLayout.ftl" as dashboardLayout />
<@dashboardLayout.header>

<style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }

    td, th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(even) {
        background-color: #dddddd;
    }
</style>

    <div id="login" class="w3-row-padding w3-padding-64 w3-container">
        <div class="w3-content">
            <h2>Search Blood Donors</h2>
            <form action="/user/searchUsers" method="post">
                <div class="container">
                    <input type="text" placeholder="City" name="city">

                    <select id="bloodGroup" name="bloodGroup">
                        <option value="A positive">A positive</option>
                        <option value="A negative">A negative</option>
                        <option value="B positive">B positive</option>
                        <option value="B negative">B negative</option>
                        <option value="AB positive">AB positive</option>
                        <option value="AB negative">AB negative</option>
                        <option value="O positive">O positive</option>
                        <option value="O negative">O negative</option>
                    </select>

                    <button type="submit" class="registerbtn">Search</button>
                </div>
            </form>
        </div>
    </div>

<table>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Mobile</th>
        <th>Age</th>
        <th>Blood Group</th>
        <th>Address</th>
    </tr>
    <#list users?reverse as user>
    <tr>
        <td>${user.name}</td>
        <td>${user.email}</td>
        <td>${user.mobile}</td>
        <td>${user.age}</td>
        <td>${user.bloodGroup}</td>
        <td>${user.address}</td>
    </tr>
    </#list>
</table>

</@dashboardLayout.header>