MAX_LEN = 100
UZ = 9
week_best = [[] for _ in range(7)]
two_best = [[] for _ in range(2)]

class Node:
    def __init__(self, w, wk):
        self.w = w
        self.wk = wk

class Node2:
    def __init__(self, name, point):
        self.name = name
        self.point = point
    def __lt__(self, other):
        return self.point < other.point

def levenshtein(a, b):
    len_a = len(a)
    len_b = len(b)
    d = [[0] * (MAX_LEN + 1) for _ in range(MAX_LEN + 1)]
    for i in range(len_a + 1):
        d[i][0] = i
    for j in range(len_b + 1):
        d[0][j] = j
    for i in range(1, len_a + 1):
        for j in range(1, len_b + 1):
            if a[i - 1] == b[j - 1]:
                d[i][j] = d[i - 1][j - 1]
            else:
                d[i][j] = 1 + min(d[i - 1][j], d[i][j - 1], d[i - 1][j - 1])
    return d[len_a][len_b]

def similer(a, b):
    global UZ
    if not a and not b:
        return True
    if not a or not b:
        return False
    dist = levenshtein(a, b)
    max_len = max(len(a), len(b))
    similarity = 1.0 - dist / max_len
    score = 1 + int(similarity * 99)
    return score >= 80

def input2(w, wk):
    global UZ
    UZ += 1
    index = 0
    if wk == "monday":
        index = 0
    elif wk == "tuesday":
        index = 1
    elif wk == "wednesday":
        index = 2
    elif wk == "thursday":
        index = 3
    elif wk == "friday":
        index = 4
    elif wk == "saturday":
        index = 5
    elif wk == "sunday":
        index = 6

    index2 = 0 if 0 <= index <= 4 else 1
    point = UZ
    max1 = 0
    max2 = 0
    flag = 0

    for node in week_best[index]:
        if node.name == w:
            max1 = node.point + node.point * 0.1
            node.point += node.point * 0.1
            flag = 1
            break

    for node in two_best[index2]:
        if node.name == w:
            max2 = node.point + node.point * 0.1
            node.point += node.point * 0.1
            break

    if UZ >= 2100000000 or max1 >= 2100000000 or max2 >= 2100000000:
        UZ = 9
        for i in range(5):
            num = 1
            for node in week_best[i]:
                node.point = num
                num += 1
        for i in range(2):
            num = 1
            for node in two_best[i]:
                node.point = num
                num += 1

    if flag == 1:
        return w

    for node in week_best[index]:
        if similer(node.name, w):
            return node.name

    for node in two_best[index2]:
        if similer(node.name, w):
            return node.name

    if len(week_best[index]) < 10:
        week_best[index].append(Node2(w, point))
        week_best[index].sort()

    if len(two_best[index2]) < 10:
        two_best[index2].append(Node2(w, point))
        two_best[index2].sort()

    if len(week_best[index]) == 10:
        if week_best[index][-1].point < point:
            week_best[index].pop()
            week_best[index].append(Node2(w, point))
            week_best[index].sort()

    if len(two_best[index2]) == 10:
        if two_best[index2][-1].point < point:
            two_best[index2].pop()
            two_best[index2].append(Node2(w, point))
            two_best[index2].sort()

    return w

def input():
    with open("keyword_weekday_500.txt") as fin:
        for _ in range(500):
            parts = fin.readline().split()
            if len(parts) != 2:
                break
            t1, t2 = parts
            ret = input2(t1, t2)
            print(ret)

if __name__ == "__main__":
    input()
