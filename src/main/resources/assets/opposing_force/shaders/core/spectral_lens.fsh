#version 150

uniform vec2 ScreenSize;
uniform float Progress;

out vec4 fragColor;

void main() {
    vec2 pixel = gl_FragCoord.xy;
    float edge = min(min(pixel.x, ScreenSize.x - pixel.x), min(pixel.y, ScreenSize.y - pixel.y)) / min(ScreenSize.x, ScreenSize.y);

    float spread = smoothstep(0.0, 0.25, Progress) * (1.0 - smoothstep(0.7, 1.0, Progress));
    float pulseWindow = smoothstep(0.2, 0.35, Progress) * (1.0 - smoothstep(0.55, 0.75, Progress));
    float pulse = 1.0 + 0.15 * sin((Progress - 0.25) * 6.2831853 * 3.0) * pulseWindow;
    float ring = 0.2 * spread * pulse;

    float softness = max(ring * 0.25, 0.002);
    float fill = 1.0 - smoothstep(ring - softness, ring, edge);
    float depth = clamp(edge / max(ring, 0.0001), 0.0, 1.0);

    float alpha = fill * mix(0.55, 0.3, depth) * smoothstep(0.0, 0.08, Progress) * (1.0 - smoothstep(0.92, 1.0, Progress));
    fragColor = vec4(0.62, 0.89, 0.82, alpha);
}
