package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.liferay.filter._
import com.arcusys.valamis.lrs.liferay.servlet._
import com.arcusys.valamis.lrs.liferay.servlet.oauth._
import com.arcusys.valamis.lrs.liferay.servlet.valamis._
import com.google.inject.servlet.ServletModule
import net.codingwell.scalaguice.ScalaModule

class WebServletModule extends ServletModule with ScalaModule {

  override def configureServlets() {

    install(LrsModule)

    filter(s"$lrsUrlPrefix/*") through classOf [LiferayContextFilter]

    val all = filter(
      s"$lrsUrlPrefix/activities/profile*",
      s"$lrsUrlPrefix/activities/state*",
      s"$lrsUrlPrefix/activity/state*",
      s"$lrsUrlPrefix/agents/profile*",
      s"$lrsUrlPrefix/statements*",
      s"$lrsUrlPrefix/activities",
      s"$lrsUrlPrefix/agents",
      s"$lrsUrlPrefix/valamis/scale",
      s"$lrsUrlPrefix/valamis/verb")

    all through classOf [MethodOverrideFilter]
    all through classOf [AuthenticationFilter]
    all through classOf [XApiVersionFilter   ]

    serve (s"$lrsUrlPrefix/valamis/verb*"      ) `with` classOf [VerbServlet           ]
    serve (s"$lrsUrlPrefix/valamis/scale*"     ) `with` classOf [ScaleServlet          ]
    serve (s"$lrsUrlPrefix/valamis/statements*") `with` classOf [StatementExtServlet   ]

    serve (s"$lrsUrlPrefix/about*"             ) `with` classOf [AboutServlet          ]
    serve (s"$lrsUrlPrefix/agents"             ) `with` classOf [AgentServlet          ]
    serve (s"$lrsUrlPrefix/activities"         ) `with` classOf [ActivityServlet       ]
    serve (s"$lrsUrlPrefix/statements"         ) `with` classOf [StatementServlet      ]
    serve (s"$lrsUrlPrefix/statements/"        ) `with` classOf [StatementServlet      ]
    serve (s"$lrsUrlPrefix/oauth/token"        ) `with` classOf [TokenServlet          ]
    serve (s"$lrsUrlPrefix/OAuth/token"        ) `with` classOf [TokenServlet          ]
    serve (s"$lrsUrlPrefix/activity/state*"    ) `with` classOf [StateProfileServlet   ]
    serve (s"$lrsUrlPrefix/agents/profile*"    ) `with` classOf [AgentProfileServlet   ]
    serve (s"$lrsUrlPrefix/oauth/initiate"     ) `with` classOf [RequestTokenServlet   ]
    serve (s"$lrsUrlPrefix/OAuth/initiate"     ) `with` classOf [RequestTokenServlet   ]
    serve (s"$lrsUrlPrefix/oauth/authorize"    ) `with` classOf [AuthorizeServlet      ]
    serve (s"$lrsUrlPrefix/OAuth/authorize"    ) `with` classOf [AuthorizeServlet      ]
    serve (s"$lrsUrlPrefix/activities/state*"  ) `with` classOf [StateProfileServlet   ]
    serve (s"$lrsUrlPrefix/activities/profile*") `with` classOf [ActivityProfileServlet]

    super.configureServlets()
  }
}

