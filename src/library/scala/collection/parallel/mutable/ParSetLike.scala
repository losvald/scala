/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala
package collection
package parallel.mutable

import scala.collection.mutable.Cloneable
import scala.collection.GenSetLike
import scala.collection.generic.Growable
import scala.collection.generic.Shrinkable

/** A template trait for mutable parallel sets. This trait is mixed in with concrete
 *  parallel sets to override the representation type.
 *
 *  $sideeffects
 *
 *  @tparam T    the element type of the set
 *  @define Coll `mutable.ParSet`
 *  @define coll mutable parallel set
 *
 *  @author Aleksandar Prokopec
 *  @since 2.9
 */
trait ParSetLike[L, T,
                 +Repr <: ParSetLike[L, T, Repr, Sequential] with ParSet[L, T],
                 +Sequential <: mutable.Set[L, T] with mutable.SetLike[L, T, Sequential]]
extends GenSetLike[L, T, Repr]
   with scala.collection.parallel.ParIterableLike[L, T, Repr, Sequential]
   with scala.collection.parallel.ParSetLike[L, T, Repr, Sequential]
   with Growable[L, T]
   with Shrinkable[L, T]
   with Cloneable[L, Repr]
{
self =>
  override def empty: Repr

  def +=(elem: T): this.type

  def -=(elem: T): this.type

  def +(elem: T) = this.clone() += elem

  def -(elem: T) = this.clone() -= elem

  // note: should not override toSet
}
