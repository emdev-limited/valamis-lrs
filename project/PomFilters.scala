import xml.{NodeSeq, Node => XNode, Elem}
import xml.transform.{RuleTransformer, RewriteRule}

object PomFilters {
  //val filterOff = Seq("valamis-lrs-data-storage", "valamis-lrs-api")

  def dependencies(n: XNode)(filterOff: Seq[String]) = new RuleTransformer(new RewriteRule {
    override def transform(n: XNode): NodeSeq = n match {
      case e: Elem if e.label == "dependency" &&
        e.child.exists(ch => ch.label == "artifactId" && filterOff.exists(ch.text.contains)) => NodeSeq.Empty
      case other => other
    }
  }).transform(n).head
}