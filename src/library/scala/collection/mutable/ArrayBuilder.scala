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

import scala.reflect.ClassTag
import scala.runtime.ScalaRunTime

/** A builder class for arrays.
 *
 *  @since 2.8
 *
 *  @tparam T    the type of the elements for the builder.
 */
abstract class ArrayBuilder[L, T] extends Builder[L, T, Array[T]] with Serializable

/** A companion object for array builders.
 *
 *  @since 2.8
 */
object ArrayBuilder {

  /** Creates a new arraybuilder of type `T`.
   *
   *  @tparam T     type of the elements for the array builder, with a `ClassTag` context bound.
   *  @return       a new empty array builder.
   */
  def make[T: ClassTag](): ArrayBuilder[L, T] = {
    val tag = implicitly[ClassTag[T]]
    tag.runtimeClass match {
      case java.lang.Byte.TYPE      => new ArrayBuilder.ofByte().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Short.TYPE     => new ArrayBuilder.ofShort().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Character.TYPE => new ArrayBuilder.ofChar().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Integer.TYPE   => new ArrayBuilder.ofInt().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Long.TYPE      => new ArrayBuilder.ofLong().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Float.TYPE     => new ArrayBuilder.ofFloat().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Double.TYPE    => new ArrayBuilder.ofDouble().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Boolean.TYPE   => new ArrayBuilder.ofBoolean().asInstanceOf[ArrayBuilder[L, T]]
      case java.lang.Void.TYPE      => new ArrayBuilder.ofUnit().asInstanceOf[ArrayBuilder[L, T]]
      case _                        => new ArrayBuilder.ofRef[T with AnyRef]()(tag.asInstanceOf[ClassTag[T with AnyRef]]).asInstanceOf[ArrayBuilder[L, T]]
    }
  }

  /** A class for array builders for arrays of reference types.
   *
   *  @tparam T     type of elements for the array builder, subtype of `AnyRef` with a `ClassTag` context bound.
   */
  @deprecatedInheritance("ArrayBuilder.ofRef is an internal implementation not intended for subclassing.", "2.11.0")
  class ofRef[T <: AnyRef : ClassTag] extends ArrayBuilder[L, T] {

    private var elems: Array[T] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[T] = {
      val newelems = new Array[T](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: T): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, T]): this.type = (xs.asInstanceOf[AnyRef]) match {
      case xs: WrappedArray.ofRef[_] =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofRef[_] => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofRef"
  }

  /** A class for array builders for arrays of `byte`s. */
  @deprecatedInheritance("ArrayBuilder.ofByte is an internal implementation not intended for subclassing.", "2.11.0")
  class ofByte extends ArrayBuilder[L, Byte] {

    private var elems: Array[Byte] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Byte] = {
      val newelems = new Array[Byte](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Byte): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Byte]): this.type = xs match {
      case xs: WrappedArray.ofByte =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofByte => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofByte"
  }

  /** A class for array builders for arrays of `short`s. */
  @deprecatedInheritance("ArrayBuilder.ofShort is an internal implementation not intended for subclassing.", "2.11.0")
  class ofShort extends ArrayBuilder[L, Short] {

    private var elems: Array[Short] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Short] = {
      val newelems = new Array[Short](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Short): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Short]): this.type = xs match {
      case xs: WrappedArray.ofShort =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofShort => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofShort"
  }

  /** A class for array builders for arrays of `char`s. */
  @deprecatedInheritance("ArrayBuilder.ofChar is an internal implementation not intended for subclassing.", "2.11.0")
  class ofChar extends ArrayBuilder[L, Char] {

    private var elems: Array[Char] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Char] = {
      val newelems = new Array[Char](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Char): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Char]): this.type = xs match {
      case xs: WrappedArray.ofChar =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofChar => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofChar"
  }

  /** A class for array builders for arrays of `int`s. */
  @deprecatedInheritance("ArrayBuilder.ofInt is an internal implementation not intended for subclassing.", "2.11.0")
  class ofInt extends ArrayBuilder[L, Int] {

    private var elems: Array[Int] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Int] = {
      val newelems = new Array[Int](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Int): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Int]): this.type = xs match {
      case xs: WrappedArray.ofInt =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofInt => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofInt"
  }

  /** A class for array builders for arrays of `long`s. */
  @deprecatedInheritance("ArrayBuilder.ofLong is an internal implementation not intended for subclassing.", "2.11.0")
  class ofLong extends ArrayBuilder[L, Long] {

    private var elems: Array[Long] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Long] = {
      val newelems = new Array[Long](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Long): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Long]): this.type = xs match {
      case xs: WrappedArray.ofLong =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofLong => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofLong"
  }

  /** A class for array builders for arrays of `float`s. */
  @deprecatedInheritance("ArrayBuilder.ofFloat is an internal implementation not intended for subclassing.", "2.11.0")
  class ofFloat extends ArrayBuilder[L, Float] {

    private var elems: Array[Float] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Float] = {
      val newelems = new Array[Float](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Float): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Float]): this.type = xs match {
      case xs: WrappedArray.ofFloat =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofFloat => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofFloat"
  }

  /** A class for array builders for arrays of `double`s. */
  @deprecatedInheritance("ArrayBuilder.ofDouble is an internal implementation not intended for subclassing.", "2.11.0")
  class ofDouble extends ArrayBuilder[L, Double] {

    private var elems: Array[Double] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Double] = {
      val newelems = new Array[Double](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Double): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Double]): this.type = xs match {
      case xs: WrappedArray.ofDouble =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofDouble => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofDouble"
  }

  /** A class for array builders for arrays of `boolean`s. */
  class ofBoolean extends ArrayBuilder[L, Boolean] {

    private var elems: Array[Boolean] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Boolean] = {
      val newelems = new Array[Boolean](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Boolean): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Boolean]): this.type = xs match {
      case xs: WrappedArray.ofBoolean =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofBoolean => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofBoolean"
  }

  /** A class for array builders for arrays of `Unit` type. */
  @deprecatedInheritance("ArrayBuilder.ofUnit is an internal implementation not intended for subclassing.", "2.11.0")
  class ofUnit extends ArrayBuilder[L, Unit] {

    private var elems: Array[Unit] = _
    private var capacity: Int = 0
    private var size: Int = 0

    private def mkArray(size: Int): Array[Unit] = {
      val newelems = new Array[Unit](size)
      if (this.size > 0) Array.copy(elems, 0, newelems, 0, this.size)
      newelems
    }

    private def resize(size: Int) {
      elems = mkArray(size)
      capacity = size
    }

    override def sizeHint(size: Int) {
      if (capacity < size) resize(size)
    }

    private def ensureSize(size: Int) {
      if (capacity < size || capacity == 0) {
        var newsize = if (capacity == 0) 16 else capacity * 2
        while (newsize < size) newsize *= 2
        resize(newsize)
      }
    }

    def +=(elem: Unit): this.type = {
      ensureSize(size + 1)
      elems(size) = elem
      size += 1
      this
    }

    override def ++=(xs: TraversableOnce[L, Unit]): this.type = xs match {
      case xs: WrappedArray.ofUnit =>
        ensureSize(this.size + xs.length)
        Array.copy(xs.array, 0, elems, this.size, xs.length)
        size += xs.length
        this
      case _ =>
        super.++=(xs)
    }

    def clear() {
      size = 0
    }

    def result() = {
      if (capacity != 0 && capacity == size) elems
      else mkArray(size)
    }

    override def equals(other: Any): Boolean = other match {
      case x: ofUnit => (size == x.size) && (elems == x.elems)
      case _ => false
    }

    override def toString = "ArrayBuilder.ofUnit"
  }
}
