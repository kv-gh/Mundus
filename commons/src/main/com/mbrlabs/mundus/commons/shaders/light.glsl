// Lighting adapted from OGLDEV tutorials https://www.youtube.com/c/OGLDEV
#ifdef GL_ES
#define LOW lowp
#define MED mediump
#define HIGH highp
precision highp float;
#else
#define MED
#define LOW
#define HIGH
#endif

struct BaseLight
{
    MED vec3 Color;
    MED vec3 AmbientColor;
    MED float AmbientIntensity;
    MED float DiffuseIntensity;
};

struct DirectionalLight
{
    BaseLight Base;
    MED vec3 Direction;
};

struct Attenuation
{
    float Constant;
    float Linear;
    float Exp;
};

struct PointLight
{
    BaseLight Base;
    MED vec3 LocalPos;
    Attenuation Atten;
};

struct SpotLight
{
    PointLight Base;
    MED vec3 Direction;
    MED float Cutoff;
};

struct Material
{
    MED vec3 AmbientColor;
    MED vec3 DiffuseColor;
    MED vec3 SpecularColor;
};

varying vec3 v_worldPos;

uniform int u_useSpecular;
uniform int u_useMaterial;
uniform int u_activeNumPointLights;
uniform int u_activeNumSpotLights;
uniform vec3 u_camPos;
uniform MED float u_shininess;
uniform DirectionalLight u_directionalLight;
uniform PointLight u_pointLights[numPointLights];
uniform SpotLight u_spotLights[numSpotLights];
uniform Material u_material;

vec4 CalcLightInternal(BaseLight Light, vec3 LightDirection, vec3 Normal)
{
    vec4 AmbientColor = vec4(Light.AmbientColor, 1.0) * Light.AmbientIntensity; /* vec4(u_material.AmbientColor, 1.0); */

    float DiffuseFactor = dot(Normal, -LightDirection);

    MED vec4 DiffuseColor = vec4(0, 0, 0, 0);
    MED vec4 SpecularColor = vec4(0, 0, 0, 0);

    if (DiffuseFactor > 0.0) {
        DiffuseColor = vec4(Light.Color, 1.0) * Light.DiffuseIntensity * DiffuseFactor;

        if (u_useMaterial == 1) {
            DiffuseColor *= vec4(u_material.DiffuseColor, 1.0);
        }

        if (u_useSpecular == 1) {
            vec3 PixelToCamera = normalize(u_camPos - v_worldPos);
            vec3 LightReflect = normalize(reflect(LightDirection, Normal));
            float SpecularFactor = dot(PixelToCamera, LightReflect);
            if (SpecularFactor > 0.0) {
                // This is for specular map textures, which we may want later but not right now.
                //float SpecularExponent = texture2D(gSamplerSpecularExponent, TexCoord0).r * 255.0;
                float SpecularExponent = u_shininess;
                SpecularFactor = pow(SpecularFactor, SpecularExponent);
                SpecularColor = vec4(Light.Color, 1.0) *
                Light.DiffuseIntensity *// using the diffuse intensity for diffuse/specular
                /* vec4(u_material.SpecularColor, 1.0) * */
                SpecularFactor;
            }
        }
    }

    return (AmbientColor + DiffuseColor + SpecularColor);
}

vec4 CalcPointLight(PointLight l, vec3 Normal)
{
    vec3 LightDirection = v_worldPos - l.LocalPos;
    float dist = length(LightDirection);
    LightDirection = normalize(LightDirection);

    vec4 Color = CalcLightInternal(l.Base, LightDirection, Normal);
    float attenuation =  l.Atten.Constant +
    l.Atten.Linear * dist +
    l.Atten.Exp * dist * dist;

    return Color / attenuation;
}

vec4 CalcSpotLight(SpotLight l, vec3 Normal)
{
    vec3 LightToPixel = normalize(v_worldPos - l.Base.LocalPos);
    float SpotFactor = dot(LightToPixel, l.Direction);

    if (SpotFactor > l.Cutoff) {
        vec4 Color = CalcPointLight(l.Base, Normal);
        float SpotLightIntensity = (1.0 - (1.0 - SpotFactor)/(1.0 - l.Cutoff));
        return Color * SpotLightIntensity;
    }
    else {
        return vec4(0,0,0,0);
    }
}

vec4 CalcDirectionalLight(vec3 Normal)
{
    return CalcLightInternal(u_directionalLight.Base, u_directionalLight.Direction, Normal);
}