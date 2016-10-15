<#include "header.ftl">

	<#include "menu.ftl">

	<!-- Page Header -->
    <!-- Set your background image for this header on the line below. -->
    <header class="intro-header" style="background-image: url('<#if (content.rootpath)??>${content.rootpath}<#else></#if>img/home-bg.jpg')">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    <div class="page-heading">
                        <h1>Archive</h1>
                        <hr class="small">
                        <span class="subheading">All posts which were ever published.</span>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
			<#list published_posts as post>
				<#if (last_month)??>
					<#if post.date?string("MMMM yyyy") != last_month>
						</ul>
						<h4>${post.date?string("MMMM yyyy")}</h4>
						<ul>
					</#if>
				<#else>
					<h4>${post.date?string("MMMM yyyy")}</h4>
					<ul>
				</#if>
		
				<li>${post.date?string("dd")} - <a href="${content.rootpath}${post.uri}"><#escape x as x?xml>${post.title}</#escape></a></li>
				<#assign last_month = post.date?string("MMMM yyyy")>
			</#list>
			</ul>
            </div>
        </div>
    </div>
	
<#include "footer.ftl">