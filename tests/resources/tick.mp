load 'tock.mp';
load 'util.mp';

function tick(n) {
	if (n <= 0) {
		return [];
	}

	return ( 'tick' : tock(n - 1) );
}

return array_join(tick(5), ' ');
