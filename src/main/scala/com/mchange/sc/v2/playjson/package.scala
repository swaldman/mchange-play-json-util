package com.mchange.sc.v2

import scala.collection._
import play.api.libs.json._

package object playjson {

  def restrictKeys( goodKeys : Set[String] )( jsv : JsValue ) : JsResult[JsValue] = {
    jsv match {
      case jso : JsObject => {
        val unexpectedKeys = jso.keys -- goodKeys
        if ( unexpectedKeys.isEmpty ) {
          JsSuccess( jsv )
        } else {
          JsError( s"""Could not read, found unexpected keys: ${unexpectedKeys.mkString(", ")}""" )
        }
      }
      case oops => JsError( s"restrinctKeys requires a JsObject, found $oops" )
    }
  }

  def provideDefaults( defaults : Map[String,JsValue] )( jsv : JsValue ) : JsResult[JsValue] = {
    jsv match {
      case jso : JsObject => {
        val defaulted = {
          defaults.foldLeft( jso ) { ( last, next ) =>
            val ( key, default ) = next
            if ( last.keys.contains( key ) ) last else last + ( key, default )
          }
        }
        JsSuccess( defaulted )
      }
      case oops => JsError( s"provideDefaults requires a JsObject, found $oops" )
    }
  }

  def restrictTransform( restrictTransformers : Seq[JsValue => JsResult[JsValue]] ) : JsValue => JsResult[JsValue] = {
    if ( restrictTransformers.length == 0 ) {
      jsv => JsSuccess( jsv )
    } else {
      jsv => restrictTransformers.tail.foldLeft( restrictTransformers.head( jsv ) )( (prevResult, next) => prevResult.flatMap( next ) )
    }
  }

  def restrictKeysWithDefaults( spec : Map[String,Option[JsValue]] ) : JsValue => JsResult[JsValue] = {
    val restrict : JsValue => JsResult[JsValue] = restrictKeys( spec.keySet )
    val default  : JsValue => JsResult[JsValue] = provideDefaults( spec.filter( _._2 != None ).map( tup => ( tup._1, tup._2.get ) ) )
    restrictTransform( restrict :: default :: Nil )
  }

  class RestrictTransformingReads[T]( restrictTransformers : Seq[JsValue => JsResult[JsValue]] )( inner : Reads[T] ) extends Reads[T] {
    def reads( jsv : JsValue ) : JsResult[T] = restrictTransform( restrictTransformers )( jsv ).flatMap( inner.reads _ )
  }

  class RestrictingDefaultingReads[T]( spec : Map[String,Option[JsValue]] )( inner : Reads[T] ) extends RestrictTransformingReads( restrictKeysWithDefaults( spec ) :: Nil )( inner )

  class RestrictTransformingFormat[T]( restrictTransformers : Seq[JsValue => JsResult[JsValue]] )( inner : Format[T] ) extends Format[T] {
    def reads( jsv : JsValue ) : JsResult[T] = restrictTransform( restrictTransformers )( jsv ).flatMap( inner.reads _ )
    def writes( t : T ) : JsValue = inner.writes(t)
  }

  class RestrictingDefaultingFormat[T]( spec : Map[String,Option[JsValue]] )( inner : Format[T] ) extends RestrictTransformingFormat( restrictKeysWithDefaults( spec ) :: Nil )( inner )
}
