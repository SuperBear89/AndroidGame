package net.jumpingman.framework.gl;

public class TextureRegion {
	public final float u1, v1;
	public final float u2, v2;
	public final Texture texture;

	public TextureRegion(Texture texture, float width, float height, int pos) {
		float x = (pos * width) % texture.width;
		double y = Math.floor((pos * width) / texture.width) * height;
		this.u1 = x / texture.width;
		this.v1 = (float)y / texture.height;
		this.u2 = this.u1 + width / texture.width;
		this.v2 = this.v1 + height / texture.height;
		this.texture = texture;
	}
}