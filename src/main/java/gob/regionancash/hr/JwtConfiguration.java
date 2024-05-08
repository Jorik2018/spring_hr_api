package gob.regionancash.hr;

import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Bean
    public JsonWebToken jsonWebToken() {
        return new JsonWebToken() {

            @Override
            public String getName() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getName'");
            }

            @Override
            public Set<String> getClaimNames() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getClaimNames'");
            }

            @Override
            public <T> T getClaim(String claimName) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getClaim'");
            }
            // Implement methods as needed
        };
    }
}