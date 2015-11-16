/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala
package collection
package mutable

import parallel.mutable.ParSeq

/** A template trait for mutable sequences of type `mutable.Seq[L, A]`.
 *  @tparam A    the type of the elements of the set
 *  @tparam This the type of the set itself.
 *
 */
trait SeqLike[L, A, +This <: SeqLike[L, A, This] with Seq[L, A]]
  extends scala.collection.SeqLike[L, A, This]
     with Cloneable[L, This]
     with Parallelizable[L, A, ParSeq[L, A]]
{
  self =>

  protected[this] override def parCombiner = ParSeq.newCombiner[A]

  /** Replaces element at given index with a new value.
   *
   *  @param idx      the index of the element to replace.
   *  @param elem     the new value.
   *  @throws   IndexOutOfBoundsException if the index is not valid.
   */
  def update(idx: Int, elem: A)

  /** Applies a transformation function to all values contained in this sequence.
   *  The transformation function produces new values from existing elements.
   *
   * @param f  the transformation to apply
   * @return   the sequence itself.
   */
  def transform(f: A => A): this.type = {
    var i = 0
    this foreach { el =>
      this(i) = f(el)
      i += 1
    }
    this
  }
}
