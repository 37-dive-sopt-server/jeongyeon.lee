package org.sopt.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sopt.config.TestConfig;
import org.sopt.domain.member.controller.MemberController;
import org.sopt.global.config.SecurityConfig;
import org.sopt.global.config.WebConfig;
import org.sopt.global.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {MemberController.class},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {WebConfig.class})
        })
@ActiveProfiles("test")
@Import({TestConfig.class, SecurityConfig.class})
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected JpaMetamodelMappingContext mappingContext;

}
