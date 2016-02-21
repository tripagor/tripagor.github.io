<#include "header.ftl">
	
	<#include "menu.ftl">


    <header class="intro-header" style="background-image: url('<#if (content.rootpath)??>${content.rootpath}<#else></#if>img/home-bg.jpg')">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    <div class="site-heading">
                        <h1>Clean Blog</h1>
                        <hr class="small">
                        <span class="subheading">A Clean Blog Theme by Start Bootstrap</span>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
				<#list posts as post>
  					<#if (post.status == "published")>
  						<div class="post-preview">
                    		<a href="${post.uri}">
                        		<h2 class="post-title">
                            		<#escape x as x?xml>${post.title}</#escape>
                        		</h2>
                        		<h3 class="post-subtitle">
                            		${post.summary}
                        		</h3>
                    		</a>
                    		<p class="post-meta">Posted by <a href="#">${post.author}</a> on ${post.date?string("dd MMMM yyyy")}</p>
                		</div>
                		<hr>
  					</#if>
  				</#list>

                <!-- Pager -->
                <ul class="pager">
                    <li class="next">
                        <a href="${content.rootpath}${config.archive_file}">Older Posts &rarr;</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>


<#include "footer.ftl">