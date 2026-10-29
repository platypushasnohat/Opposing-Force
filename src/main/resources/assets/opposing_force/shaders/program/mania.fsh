#version 150

uniform sampler2D DiffuseSampler;

uniform float Time;
uniform float Strength;
uniform float Mode;

in vec2 texCoord;

out vec4 fragColor;

vec2 wobble(float phase, float radius) {
    float t = Time * 6.2831853;
    return vec2(sin(t * 2.0 + phase), cos(t * 3.0 + phase * 1.7)) * radius;
}

float luma(vec3 color) {
    return dot(color, vec3(0.299, 0.587, 0.114));
}

void main() {
    float radius = mix(0.022, 0.05, Mode) * Strength;
    vec2 wave = vec2(sin(texCoord.y * 18.0 + Time * 25.132741), cos(texCoord.x * 14.0 + Time * 18.849556)) * mix(0.0045, 0.012, Mode) * Strength;
    vec2 uv = texCoord + wave;

    vec2 cyanOffset = wobble(0.0, radius) + vec2(radius, 0.0);
    vec2 magentaOffset = wobble(2.1, radius) - vec2(radius, 0.0);
    vec2 yellowOffset = mix(cyanOffset, wobble(4.2, radius) + vec2(0.0, radius), Mode);
    vec2 keyOffset = wobble(5.3, radius) - vec2(0.0, radius);

    vec3 base = texture(DiffuseSampler, texCoord).rgb;
    float cyan = texture(DiffuseSampler, uv + cyanOffset).r;
    float magenta = texture(DiffuseSampler, uv + magentaOffset).g;
    float yellow = texture(DiffuseSampler, uv + yellowOffset).b;
    vec3 plates = vec3(cyan, magenta, yellow);

    vec3 keySample = texture(DiffuseSampler, uv + keyOffset).rgb;
    float key = 1.0 - max(max(keySample.r, keySample.g), keySample.b);
    plates *= 1.0 - key * 0.6 * Mode;

    vec3 ghost = texture(DiffuseSampler, uv - cyanOffset * 1.5).rgb;
    vec3 tripped = mix(plates, ghost, 0.35);

    float purple = luma(texture(DiffuseSampler, uv + wobble(1.3, radius * 1.6) + vec2(radius * 1.4, radius)).rgb);
    float green = luma(texture(DiffuseSampler, uv + wobble(3.4, radius * 1.6) - vec2(radius * 1.4, -radius)).rgb);
    float blue = luma(texture(DiffuseSampler, uv + wobble(4.9, radius * 1.6) - vec2(0.0, radius * 1.8)).rgb);
    vec3 extra = purple * vec3(0.54, 0.17, 0.89) + green * vec3(0.17, 1.0, 0.29) + blue * vec3(0.17, 0.36, 1.0);
    tripped = mix(tripped, tripped * 0.45 + extra * 0.75, Mode * 0.7);

    float sweep = 0.5 + 0.5 * sin(Time * 12.5663706 + texCoord.y * 12.0);
    vec3 tint = mix(vec3(0.0, 1.0, 1.0), vec3(1.0, 0.0, 1.0), sweep);
    float band = 0.5 + 0.5 * sin(Time * 6.2831853 + texCoord.x * 9.0);
    vec3 modeTint = mix(vec3(0.54, 0.17, 0.89), mix(vec3(0.17, 1.0, 0.29), vec3(0.17, 0.36, 1.0), sweep), band);
    tint = mix(tint, modeTint, Mode);
    tripped = mix(tripped, tripped * tint + (1.0 - tint) * 0.1, mix(0.32, 0.5, Mode));

    vec2 centered = texCoord - 0.5;
    float pulse = 0.5 + 0.5 * sin(Time * 18.849556);
    tripped *= 1.0 - dot(centered, centered) * (0.8 + 0.6 * pulse) * mix(0.75, 1.0, Mode) * Strength;

    fragColor = vec4(mix(base, tripped, Strength), 1.0);
}
