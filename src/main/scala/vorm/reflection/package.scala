package vorm

import reflect.mirror

package object reflection {

  private val tpeCache =
    collection.mutable.Map[mirror.Type, Type]()

  private[reflection] def tpe[T](mt: mirror.Type, jc: Option[Class[_]] = None): Type =
    try tpeCache(mt)
    catch {
      case _ =>
        val t = new Type(mt, jc)
        tpeCache.update(mt, t)
        t
    }

  def tpe[T: TypeTag]: Type =
    tag[T] match { case t => tpe(t.tpe, Some(t.erasure)) }



  implicit class AnyAdapter
    [ T : TypeTag ]
    ( any : T )
    {
      def reflected
        = new Reflected( any, Reflection( typeTag[T] ) )
      def isInstanceOf
        ( r : Reflection )
        : Boolean
        = reflected.reflection inheritsFrom r
    }



  implicit class ClassAdapter
    [ T ]
    ( c : Class[T] )
    {
      def instantiate
        ( args : Seq[Any] )
        : T
        = c 
          .getConstructors.head
          .newInstance(args.asInstanceOf[Seq[Object]]: _*)
          .asInstanceOf[T]
    }



}