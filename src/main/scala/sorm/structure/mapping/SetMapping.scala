package sorm.structure.mapping

import sorm._
import sext.Sext._
import reflection._
import ddl._
import structure._

sealed class SetMapping
  ( val membership : Option[Membership],
    val reflection : Reflection,
    settingsMap : SettingsMap )
  extends CollectionMapping
  {
    lazy val item : Mapping
      = Mapping( Membership.SetItem(this), reflection.generics(0), settingsMap )

    lazy val children : Set[Mapping]
      = Set( item )

    lazy val primaryKeyColumns : IndexedSeq[Column]
      = containerTableColumns :+ hashColumn

    lazy val hashColumn : Column
      = Column( "h", Column.Type.Integer )

    lazy val columns
      = primaryKeyColumns ++: childrenColumns

  }
