package com.mchange.sc.v2.playjson

import java.io.InputStream
import play.api.libs.json._

import java.nio.ByteBuffer

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
    def toJsValue( bb : ByteBuffer ) : JsValue = Json.parse( newArray( bb ) )
  }

  // translated from mchange-commons-java com.mchange.v3.nio.ByteBufferUtils.newArray(...) to avoid
  // a dependency on that library for just this
  //
  // if this library eventually comes to depend on mchange-commons-java (or mchange-commons-scala,
  // which depends on mchange-commons-java), then get rid of this and just use that function

  private def newArray( bb : ByteBuffer ) : Array[Byte] = {
    if ( bb.hasArray() ) {
      bb.array().clone().asInstanceOf[Array[Byte]];
    }
    else {
      val out = Array.ofDim[Byte](bb.remaining())
      bb.get(out)
      out;
    }
  }
}
trait JsValueSource[T] {
  def toJsValue( t : T ) : JsValue
}
