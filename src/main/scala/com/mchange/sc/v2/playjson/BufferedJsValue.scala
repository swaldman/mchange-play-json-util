package com.mchange.sc.v2.playjson

import scala.io.Codec
import scala.collection._

import com.mchange.v1.io.InputStreamUtils
import com.mchange.sc.v2.collection.immutable.ImmutableArraySeq

import play.api.libs.json._

import java.io.InputStream
import java.nio.ByteBuffer

import com.mchange.v3.nio.ByteBufferUtils

object BufferedJsValue {
  def apply( arr : Array[Byte] ) : BufferedJsValue = this.apply( ImmutableArraySeq.Byte(arr) ) // this could be from anywhere, better copy it
  def apply( jsv : JsValue     ) : BufferedJsValue = this.apply( Json.stringify( jsv ) )
  def apply( is  : InputStream ) : BufferedJsValue = this.apply( ImmutableArraySeq.Byte.createNoCopy( InputStreamUtils.getBytes( is ) ) )
  def apply( str : String      ) : BufferedJsValue = this.apply( ImmutableArraySeq.Byte.createNoCopy( str.getBytes( Codec.UTF8.charSet ) ) )
  def apply( bb  : ByteBuffer  ) : BufferedJsValue = this.apply( ImmutableArraySeq.Byte.createNoCopy( ByteBufferUtils.newArray( bb ) ) )
}
case class BufferedJsValue( buffer : immutable.Seq[Byte] ) {
  def toJsValue : JsValue = Json.parse( buffer.toArray )

  def transform( transformer : immutable.Seq[Byte] => immutable.Seq[Byte] ) : BufferedJsValue = BufferedJsValue( transformer( buffer ) )
}
