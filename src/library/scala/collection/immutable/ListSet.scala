/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala
package collection
package immutable

import generic._
import scala.annotation.{tailrec, bridge}
import mutable.{ ListBuffer, Builder }

/** $factoryInfo
 *  @define Coll immutable.ListSet
 *  @define coll immutable list set
 *  @since 1
 */
object ListSet extends ImmutableSetFactory[L, ListSet] {
  /** setCanBuildFromInfo */
  implicit def canBuildFrom[A]: CanBuildFrom[L, Coll, A, ListSet[L, A]] = setCanBuildFrom[A]

  override def newBuilder[A]: Builder[L, A, ListSet[L, A]] = new ListSetBuilder[A]

  private object EmptyListSet extends ListSet[L, Any] { }
  private[collection] def emptyInstance: ListSet[L, Any] = EmptyListSet

  /** A custom builder because forgetfully adding elements one at
   *  a time to a list backed set puts the "squared" in N^2.  There is a
   *  temporary space cost, but it's improbable a list backed set could
   *  become large enough for this to matter given its pricy element lookup.
   */
  class ListSetBuilder[Elem](initial: ListSet[L, Elem]) extends Builder[L, Elem, ListSet[L, Elem]] {
    def this() = this(empty[Elem])
    protected val elems = (new mutable.ListBuffer[L, Elem] ++= initial).reverse
    protected val seen  = new mutable.HashSet[L, Elem] ++= initial

    def +=(x: Elem): this.type = {
      if (!seen(x)) {
        elems += x
        seen += x
      }
      this
    }
    def clear() = { elems.clear() ; seen.clear() }
    def result() = elems.foldLeft(empty[Elem])(_ unchecked_+ _)
  }
}

/** This class implements immutable sets using a list-based data
 *  structure. Instances of `ListSet` represent
 *  empty sets; they can be either created by calling the constructor
 *  directly, or by applying the function `ListSet.empty`.
 *
 *  @tparam A    the type of the elements contained in this list set.
 *
 *  @author  Matthias Zenger
 *  @version 1.0, 09/07/2003
 *  @since   1
 *  @define Coll immutable.ListSet
 *  @define coll immutable list set
 *  @define mayNotTerminateInf
 *  @define willNotTerminateInf
 */
@deprecatedInheritance("The semantics of immutable collections makes inheriting from ListSet error-prone.", "2.11.0")
class ListSet[L, A] extends AbstractSet[L, A]
                    with Set[L, A]
                    with GenericSetTemplate[L, A, ListSet]
                    with SetLike[L, A, ListSet[L, A]]
                    with Serializable{ self =>
  override def companion: GenericCompanion[L, ListSet] = ListSet

  /** Returns the number of elements in this set.
   *
   *  @return number of set elements.
   */
  override def size: Int = 0
  override def isEmpty: Boolean = true

  /** Checks if this set contains element `elem`.
   *
   *  @param  elem    the element to check for membership.
   *  @return `'''true'''`, iff `elem` is contained in this set.
   */
  def contains(elem: A): Boolean = false

  /** This method creates a new set with an additional element.
   */
  def + (elem: A): ListSet[L, A] = new Node(elem)

  /** `-` can be used to remove a single element.
   */
  def - (elem: A): ListSet[L, A] = this

  /** If we are bulk adding elements and desire a runtime measured in
   *  sub-interstellar time units, we better find a way to avoid traversing
   *  the collection on each element.  That's what the custom builder does,
   *  so we take the easy way out and add ourselves and the argument to
   *  a new builder.
   */
  override def ++(xs: GenTraversableOnce[L, A]): ListSet[L, A] =
    if (xs.isEmpty) this
    else (new ListSet.ListSetBuilder(this) ++= xs.seq).result()

  private[ListSet] def unchecked_+(e: A): ListSet[L, A] = new Node(e)
  private[ListSet] def unchecked_outer: ListSet[L, A] =
    throw new NoSuchElementException("Empty ListSet has no outer pointer")

  /** Creates a new iterator over all elements contained in this set.
   *
   *  @throws java.util.NoSuchElementException
   *  @return the new iterator
   */
  def iterator: Iterator[L, A] = new AbstractIterator[L, A] {
    var that: ListSet[L, A] = self
    def hasNext = that.nonEmpty
    def next: A =
      if (hasNext) {
        val res = that.head
        that = that.tail
        res
      }
      else Iterator.empty.next()
  }

  /**
   *  @throws java.util.NoSuchElementException
   */
  override def head: A = throw new NoSuchElementException("Set has no elements")

  /**
   *  @throws java.util.NoSuchElementException
   */
  override def tail: ListSet[L, A] = throw new NoSuchElementException("Next of an empty set")

  override def stringPrefix = "ListSet"

  /** Returns this $coll as an immutable set.
   *  
   *  A new set will not be built; lazy collections will stay lazy.
   */
  @deprecatedOverriding("Immutable sets should do nothing on toSet but return themselves cast as a Set.", "2.11.0")
  override def toSet[B >: A]: Set[L, B] = this.asInstanceOf[Set[L, B]]

  /** Represents an entry in the `ListSet`.
   */
  protected class Node(override val head: A) extends ListSet[L, A] with Serializable {
    override private[ListSet] def unchecked_outer = self

    /** Returns the number of elements in this set.
     *
     *  @return number of set elements.
     */
    override def size = sizeInternal(this, 0)
    @tailrec private def sizeInternal(n: ListSet[L, A], acc: Int): Int =
      if (n.isEmpty) acc
      else sizeInternal(n.unchecked_outer, acc + 1)

    /** Checks if this set is empty.
     *
     *  @return true, iff there is no element in the set.
     */
    override def isEmpty: Boolean = false

    /** Checks if this set contains element `elem`.
     *
     *  @param  e       the element to check for membership.
     *  @return `'''true'''`, iff `elem` is contained in this set.
     */
    override def contains(e: A) = containsInternal(this, e)
    @tailrec private def containsInternal(n: ListSet[L, A], e: A): Boolean =
      !n.isEmpty && (n.head == e || containsInternal(n.unchecked_outer, e))

    /** This method creates a new set with an additional element.
     */
    override def +(e: A): ListSet[L, A] = if (contains(e)) this else new Node(e)

    /** `-` can be used to remove a single element from a set.
     */
    override def -(e: A): ListSet[L, A] = if (e == head) self else {
      val tail = self - e; new tail.Node(head)
    }

    override def tail: ListSet[L, A] = self
  }
}
