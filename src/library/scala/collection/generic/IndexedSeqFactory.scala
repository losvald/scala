/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala
package collection
package generic

import language.higherKinds

/** A template for companion objects of IndexedSeq and subclasses thereof.
 *
 *  @since 2.11
 */
abstract class IndexedSeqFactory[L, CC[X] <: IndexedSeq[L, X] with GenericTraversableTemplate[L, X, CC]] extends SeqFactory[L, CC] {
  override def ReusableCBF: GenericCanBuildFrom[Nothing] =
    scala.collection.IndexedSeq.ReusableCBF.asInstanceOf[GenericCanBuildFrom[Nothing]]
}
