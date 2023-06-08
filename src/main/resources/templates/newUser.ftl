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

    <div id="newUser" class="w3-row-padding w3-padding-64 w3-container">
        <div class="w3-content">
            <h2>Your Donor Details</h2>
            <form action="/saveUpdateUser" method="post">
                <div class="container">
                    <input type="text" placeholder="Full Name" name="name">
                    <input type="text" placeholder="Email" name="email">
                    <input type="text" placeholder="Age" name="age">
                    <input type="text" placeholder="Address" name="address">
                    <input type="text" placeholder="Mobile number" name="mobile">
                    <select id="bloodGroup" name="bloodGroup">
                        <option value="A positive">A positive</option>
                        <option value="A negative">A negative</option>
                        <option value="B positive">B positive</option>
                        <option value="B negative">B negative</option>
                        <option value="AB positive">AB positive</option>
                        <option value="AB negative">AB negative</option>
                    </select>

                    <button type="submit" class="registerbtn">Submit</button>
                </div>
            </form>
        </div>
    </div>

</@dashboardLayout.header>