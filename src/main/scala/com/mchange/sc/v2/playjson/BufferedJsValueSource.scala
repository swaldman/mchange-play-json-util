package com.mchange.sc.v2.playjson

import java.io.InputStream
import java.nio.ByteBuffer

import play.api.libs.json.JsValue

object BufferedJsValueSource {
  implicit final object JsValueIsBufferedSource extends BufferedJsValueSource[JsValue] {
    def toBufferedJsValue( jsv : JsValue ) : BufferedJsValue = BufferedJsValue( jsv )
  }
  implicit final object StringIsBufferedSource extends BufferedJsValueSource[String] {
    def toBufferedJsValue( str : String ) : BufferedJsValue = BufferedJsValue( str )
  }
  implicit final object ByteArrayIsBufferedSource extends BufferedJsValueSource[Array[Byte]] {
    def toBufferedJsValue( arr : Array[Byte] ) : BufferedJsValue = BufferedJsValue( arr )
  }
  implicit final object SeqIsBufferedSource extends BufferedJsValueSource[Seq[Byte]] {
    def toBufferedJsValue( seq : Seq[Byte] ) : BufferedJsValue = BufferedJsValue( seq )
  }
  implicit final object InputStreamIsBufferedSource extends BufferedJsValueSource[InputStream] {
    def toBufferedJsValue( is : InputStream ) : BufferedJsValue = BufferedJsValue( is )
  }
  implicit final object ByteBufferIsBufferedSource extends BufferedJsValueSource[ByteBuffer] {
    def toBufferedJsValue( bb : ByteBuffer ) : BufferedJsValue = BufferedJsValue( bb )
  }
}
trait BufferedJsValueSource[T] {
  def toBufferedJsValue( t : T ) : BufferedJsValue
}
