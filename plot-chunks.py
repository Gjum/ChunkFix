import matplotlib.pyplot as plt
import re
import colorsys

lr = re.compile('.*test02 got chunk at #,# which is #,# away, player: #,#.*'.replace('#', '([0-9-]+)'))

points = []
for line in open('run/logs/latest.log'):
    match = lr.match(line)
    if match:
        cx, cz, dx, dz, pcx, pcz = map(int, match.groups())
        points.append((pcx - cx, pcz - cz))

npoints = len(points)
colors = [colorsys.hsv_to_rgb(i / npoints, 1, 1) for i in range(npoints)]

px, pz = zip(*points)
plt.scatter(px, pz, color=colors[:len(points)])

plt.show()
