package io.github.cloudlet.domain.cloud;

public class CloudEffects {

    private boolean hasShield    = false;
    private float   shieldTimer  = 0f;
    private float   magnetTimer  = 0f;
    private float   rainbowTimer = 0f;
    private float   windRotation = 0f;
    private boolean blocked      = false;
    private float invertedTimer = 0f;

    public boolean hasShield()             { return hasShield; }
    public void    setShield(boolean v)    { hasShield = v; }
    public float   getShieldTimer()        { return shieldTimer; }
    public void    setShieldTimer(float v) { shieldTimer = v; }
    public float   getMagnetTimer()        { return magnetTimer; }
    public void    setMagnetTimer(float v) { magnetTimer = v; }
    public float   getRainbowTimer()       { return rainbowTimer; }
    public void    setRainbowTimer(float v){ rainbowTimer = v; }
    public float   getWindRotation()       { return windRotation; }
    public void    setWindRotation(float v){ windRotation = v; }
    public boolean isBlocked()             { return blocked; }
    public void    setBlocked(boolean v)   { blocked = v; }
    public float   getInvertedTimer()        { return invertedTimer; }
    public void    setInvertedTimer(float v) { invertedTimer = v; }
    public boolean isInverted()              { return invertedTimer > 0f; }
}
