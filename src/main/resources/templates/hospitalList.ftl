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
            <h2>Search Hospitals and Blood-banks</h2>
            <form action="/user/searchHospital" method="post">
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
        <th>Hospital/Blood-bank Name</th>
        <th>Email</th>
        <th>Address</th>
        <th>Phone</th>
        <th>Available Blood</th>
    </tr>
    <#list hospitalList?reverse as hospital>
    <tr>
        <td>${hospital.name}</td>
        <td>${hospital.email}</td>
        <td>${hospital.address}</td>
        <td>${hospital.phone}</td>
        <td>${hospital.bloodGroup}</td>
    </tr>
    </#list>
</table>

</@dashboardLayout.header>