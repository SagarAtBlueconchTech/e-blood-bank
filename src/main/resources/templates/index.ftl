<#-- @ftlvariable name="users" type="kotlin.collections.List<com.example.models.Article>" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <#list users?reverse as user>
        <div>
            <h3>
<#--                <a href="/articles/${user.id}">${user.title}</a>-->
                <a href="#">${user.name}</a>
            </h3>
            <p>
                ${user.bloodGroup}
            </p>
        </div>
    </#list>
    <hr>
    <p>
        <a href="/articles/new">Create article</a>
    </p>
</@layout.header>