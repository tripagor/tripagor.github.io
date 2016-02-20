<#include "header.ftl">
	
	<#include "menu.ftl">

	 <header class="intro-header" style="background-image: url('img/post-bg.jpg')">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    <div class="post-heading">
                        <h1><#escape x as x?xml>${content.title}</#escape></h1>
                        <h2 class="subheading"><#escape x as x?xml>${content.summary}</#escape></h2>
                        <span class="meta">Posted by <a href="#"><#escape x as x?xml>${content.author}</#escape></a> on ${content.date?string("dd MMMM yyyy")}</span>
                    </div>
                </div>
            </div>
        </div>
    </header>
 	<article>
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    ${content.body}
                </div>
            </div>
        </div>
    </article>
	<hr />
	
<#include "footer.ftl">