<#import "homePageLayout.ftl" as homePageLayout />
<@homePageLayout.header>

    <!-- First Grid -->
    <div id="login" class="w3-row-padding w3-padding-64 w3-container">
        <div class="w3-content">
            <h2>Login Form</h2>

            <form action="/login" method="post">

                <div class="container">
                    <label for="uname"><b>Email</b></label>
                    <input type="text" placeholder="Enter email" name="email" required>

                    <label for="psw"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="password" required>

                    <label for="userType"><b>User Type</b></label>
                    <select id="userType" name="usertype" required>
                        <option value="user">User</option>
                        <option value="hospital">Hospital</option>
                    </select>

                    <button type="submit" class="registerbtn">Login</button>
                </div>

                <div class="container signin">
                    <p>Not registered yet? <a href="#register">Register</a>.</p>
                </div>
            </form>
        </div>
    </div>

    <!-- Second Grid -->
    <div id="register" class="w3-row-padding w3-light-grey w3-padding-64 w3-container">
        <div class="w3-content">
            <form action="/register" method="post">
                <div class="container">
                    <h1>Register</h1>
                    <p>Please fill in this form to create an account.</p>
                    <hr>

                    <label for="email"><b>Email</b></label>
                    <input type="text" placeholder="Enter Email" name="email" id="email" required>

                    <label for="psw"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="password" id="psw" required>

                    <label for="userType"><b>User Type</b></label>
                    <select id="userType" name="usertype" required>
                        <option value="user">User</option>
                        <option value="hospital">Hospital</option>
                    </select>
                    <hr>
                    <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
                    <button type="submit" class="registerbtn">Register</button>
                </div>

                <div class="container signin">
                    <p>Already have an account? <a href="#login">Sign in</a>.</p>
                </div>
            </form>
        </div>
    </div>


</@homePageLayout.header>