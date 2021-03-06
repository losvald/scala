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

/** A template trait that contains just the `map`, `flatMap`, `foreach` and `withFilter` methods
 *  of trait `TraversableLike`.
 */
trait FilterMonadic[+A, +Repr] extends Any { self =>
  type LT
  type MaybeCanThrow = CannotThrow
  @local private implicit def mct0 = new CannotThrow {} // TODO(leo) re-think
  def map[B, That](f: A => B)(implicit bf: CanBuildFrom[Repr, B, That], @local mct: MaybeCanThrow): That
  def flatMap[B, That](f: A => scala.collection.GenTraversableOnce[B])(implicit bf: CanBuildFrom[Repr, B, That], @local mct: MaybeCanThrow): That
  def foreach[U](f: A => U)(implicit @local mct: MaybeCanThrow): Unit
  def withFilter(p: A => Boolean)(implicit @local mct: MaybeCanThrow = mct0): FilterMonadic[A, Repr]
}
