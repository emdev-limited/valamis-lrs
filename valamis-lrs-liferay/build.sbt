tomcat(port = 8090, options = ForkOptions.apply(
  runJVMOptions = Seq(
    "-Xdebug",
    "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")))