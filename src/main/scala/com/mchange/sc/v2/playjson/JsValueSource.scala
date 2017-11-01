package com.mchange.sc.v2.playjson

import java.io.InputStream
import play.api.libs.json._

import java.nio.ByteBuffer

import com.mchange.v3.nio.ByteBufferUtils

object JsValueSource {
  implicit final object JsValueIsSource extends JsValueSource[JsValue] {
    def toJsValue( jsv : JsValue ) : JsValue = jsv
  }
  implicit final object StringIsSource extends JsValueSource[String] {
    def toJsValue( str : String ) : JsValue = Json.parse( str )
  }
  implicit final object ByteArrayIsSource extends JsValueSource[Array[Byte]] {
    def toJsValue( arr : Array[Byte] ) : JsValue = Json.parse( arr )
  }
  implicit final object SeqIsSource extends JsValueSource[Seq[Byte]] {
    def toJsValue( seq : Seq[Byte] ) : JsValue = Json.parse( seq.toArray )
  }
  implicit final object InputStreamIsSource extends JsValueSource[InputStream] {
    def toJsValue( is : InputStream ) : JsValue = Json.parse( is )
  }
  implicit final object ByteBufferIsSource extends JsValueSource[ByteBuffer] {
    def toJsValue( bb : ByteBuffer ) : JsValue = Json.parse( ByteBufferUtils.newArray( bb ) )
  }
}
trait JsValueSource[T] {
  def toJsValue( t : T ) : JsValue
}
