package scala.pickling
package internal

import java.util.concurrent.locks.ReentrantLock

import scala.pickling.refs.Share
import scala.pickling.{Pickler, Unpickler, FastTypeTag}
import scala.pickling.spi.{PicklerRegistry, RefRegistry, PicklingRuntime}
import scala.reflect.runtime

/**
 * An implementation of the pickling runtime that tries to avoid ALL runtime picklers.
 */
final class NoReflectionRuntime() extends PicklingRuntime {
  /** The current reflection mirror to use when doing runtime unpickling/pickling. */
  override def currentMirror: runtime.universe.Mirror = runtime.currentMirror

  object picklers extends PicklerRegistry {
    override def genUnpickler(mirror: runtime.universe.Mirror, tagKey: String)(implicit share: refs.Share): _root_.scala.pickling.Unpickler[_] =
      throw new UnsupportedOperationException(s"Runtime pickler generation is disabled.  Cannot create unpickler for $tagKey")
    override def genPickler(classLoader: ClassLoader, clazz: Class[_], tag: FastTypeTag[_])(implicit share: Share): Pickler[_] =
      throw new UnsupportedOperationException(s"Runtime pickler generation is disabled.  Cannot create pickler for $tag")
    override def registerPickler(key: String, p: Pickler[_]): Unit = ()
    override def lookupUnpickler(key: String): Option[Unpickler[_]] = None
    override def registerUnpickler(key: String, p: Unpickler[_]): Unit = ()
  }
  override val refRegistry: RefRegistry = new DefaultRefRegistry()
  override val GRL: ReentrantLock = new ReentrantLock()
  override def makeFastTag[T](tagKey: String): FastTypeTag[T] = FastTypeTag.apply(currentMirror, tagKey).asInstanceOf[FastTypeTag[T]]
}