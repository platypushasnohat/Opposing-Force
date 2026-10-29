#version 150

uniform vec2 ScreenSize;
uniform float Progress;

out vec4 fragColor;

float roundedBox(vec2 point, vec2 halfSize, float radius) {
    vec2 corner = abs(point) - halfSize + radius;
    return length(max(corner, 0.0)) + min(max(corner.x, corner.y), 0.0) - radius;
}

void main() {
    float scale = min(ScreenSize.x, ScreenSize.y);
    vec2 point = (gl_FragCoord.xy - ScreenSize * 0.5) / scale;
    vec2 halfSize = ScreenSize * 0.5 / scale;

    float spread = smoothstep(0.0, 0.25, Progress) * (1.0 - smoothstep(0.7, 1.0, Progress));
    float pulseWindow = smoothstep(0.2, 0.35, Progress) * (1.0 - smoothstep(0.55, 0.75, Progress));
    float pulse = 1.0 + 0.15 * sin((Progress - 0.25) * 6.2831853 * 3.0) * pulseWindow;
    float ring = 0.14 * spread * pulse;

    float outside = roundedBox(point, halfSize - ring, 0.2);
    float fade = ring * 1.5 + 0.001;
    float strength = clamp((outside + fade) / (ring + fade), 0.0, 1.0);

    float alpha = strength * strength * 0.6 * spread;
    fragColor = vec4(0.2, 0.68, 0.78, alpha);
}