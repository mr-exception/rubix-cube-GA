generation_size =       [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]
kill_size =             [0, 0.5, 0.1, 0.2, 0.3, 0.5, 0.6, 0.7]
random_save_rate =      [0, 0.5, 0.1, 0.2, 0.3, 0.5, 0.6, 0.7]
chromose_size =         [18, 19, 20, 21, 22, 23, 24, 25]
use_RB =                [0, 1]

def generate():
    global generation_size

    test = []
    for generation in generation_size:
        test += choose_kill_rate(generation)
    return test

def choose_kill_rate(generation):
    global kill_size
    test = []
    for kill in kill_size:
        test += choose_random_save(generation, int(kill * generation))
    return test


def choose_random_save(generation, kill):
    global random_save_rate
    test = []
    for random_save in random_save_rate:
        test += choose_chromosome_size(generation, kill, int(random_save * kill))
    return test

def choose_chromosome_size(generation, kill, random_save):
    global chromose_size
    test = []
    for chromosome in chromose_size:
        test += choose_use_RB(generation, kill, random_save, chromosome)
    return test

def choose_use_RB(generation, kill, random_save, chromosome):
    global use_RB 
    test = []
    for RB in use_RB:
        test.append([generation, kill, random_save, chromosome, RB])
    return test

tests = generate()
file = open('java/tests.txt', 'w+');
for test in tests:
    file.write('{0},{1},{2},{3},{4}\n'.format(
            test[0],
            test[1],
            test[2],
            test[3],
            test[4]
        ))
file.close()
print("wrote {0} test cases in tests.txt".format(len(tests)));