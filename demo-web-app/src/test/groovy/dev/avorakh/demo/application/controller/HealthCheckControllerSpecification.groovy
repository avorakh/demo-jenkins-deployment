package dev.avorakh.demo.application.controller

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest
class HealthCheckControllerSpecification extends Specification {
    @Autowired
    private MockMvc mockMvc

    def 'should successfully return OK status code and success with true value in response body'() {

        expect:
        mockMvc.perform(get('/status').accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.success').isBoolean())
                .andExpect(jsonPath('$.success').value(true))
    }
}
