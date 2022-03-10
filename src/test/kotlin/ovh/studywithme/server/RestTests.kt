package ovh.studywithme.server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.http.HttpEntity
import org.junit.jupiter.api.Tag
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import java.net.URI

@ExtendWith(SpringExtension::class)
@Tag("integration")
open class RestTests(){

	val mapper = jacksonObjectMapper()

	inline fun <reified T : Any> get(path:String, trt:TestRestTemplate, port:Int): T {
		return trt.getForObject<T>(
			URI("http://localhost:" + port + path),
			T::class.java)
	}

    inline fun <S,reified T : Any> post(path:String, payload:S, trt:TestRestTemplate, port:Int): T {
		return trt.postForObject<T>(
			URI("http://localhost:" + port + path),
            HttpEntity(payload),
			T::class.java)
	}

	fun <S> put(path:String, payload:S, trt:TestRestTemplate, port:Int) {
		return trt.put(
			URI("http://localhost:" + port + path),
            HttpEntity(payload))
	}

    fun delete(path:String, trt:TestRestTemplate, port:Int) {
		return trt.delete(
			URI("http://localhost:" + port + path))
	}

    fun getBody(response: ResponseEntity<String>):String{
        val output: String? = response.body
        if (output!=null){
            return output
        }
        return ""
    }

	inline fun <reified T> getBodyObj(response: ResponseEntity<String>):T?{
		val output: String? = response.body
		if (output!=null){
			return mapper.readValue(output, T::class.java)
		}
		return null
	}

	fun getEx(path:String,trt:TestRestTemplate, port:Int): ResponseEntity<String> {
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	}

	fun <T> putEx(path:String, payload:T,trt:TestRestTemplate, port:Int):ResponseEntity<String>{
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.PUT,
			HttpEntity(payload),
			String::class.java)
	}

	fun <T> postEx(path:String, payload:T,trt:TestRestTemplate, port:Int):ResponseEntity<String>{
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.POST,
			HttpEntity(payload),
			String::class.java)
	}

	fun deleteEx(path:String,trt:TestRestTemplate, port:Int): ResponseEntity<String> {
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.DELETE,
			HttpEntity(""),
			String::class.java)
	}
}