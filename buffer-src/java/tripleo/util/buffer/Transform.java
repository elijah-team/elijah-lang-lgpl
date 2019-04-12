package tripleo.util.buffer;

import tripleo.elijah.gen.nodes.ArgumentNode;

public interface Transform {

	void transform(ArgumentNode na, DefaultBuffer bufbldr);

}