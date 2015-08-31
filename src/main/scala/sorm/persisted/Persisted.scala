package sorm.persisted

import reflect.runtime.universe._

import sorm._
import reflection._

import sext._, embrace._

object Persisted {

  def apply[T <: Persistable](instance : T, id : Long): T
    = throw new Exception("Persisted on persisted called")

  def apply[T <: Persistable : TypeTag](instance : T, id : Long): T
    = apply(instance.reflected, id )

  def apply[T](reflected : Reflected, id : Long): T
    = apply(reflected.propertyValues, id, reflected.reflection) .asInstanceOf[T]

  def apply[T: TypeTag](args : Map[String, Any], id : Long): T
    = apply( args, id, Reflection[T]).asInstanceOf[T]

  def apply(args : Map[String, Any], id: Long, r: Reflection ): Persistable
    = r.instantiate(r.primaryConstructorArguments.toStream.unzip._1.map{args.updated("id", Some(id))})
}