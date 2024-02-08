package com.epam.dmgolub.gym.security.filter;

import com.epam.dmgolub.gym.security.service.TokenService;
import com.epam.dmgolub.gym.security.utility.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	private final UserDetailsService userDetailsService;
	private final TokenService tokenService;

	public TokenAuthenticationFilter(final UserDetailsService userDetailsService, final TokenService tokenService) {
		this.userDetailsService = userDetailsService;
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final FilterChain filterChain
	) throws ServletException, IOException {
		SecurityUtils.extractBearerToken(request).ifPresent(token -> {
			final String userName = tokenService.extractUsername(token);
			final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (tokenService.isValidToken(token, userDetails.getUsername())) {
				final UsernamePasswordAuthenticationToken authToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				LOGGER.debug("In doFilterInternal - Successfully authenticated user with userName={}", userName);
			}}
		);
		filterChain.doFilter(request, response);
	}
}
