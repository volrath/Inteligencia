#!/usr/bin/env python
import struct

AGE = [30, 55, 72, 90]
WORKCLASS = ['Private', 'Self-emp-not-inc', 'Self-emp-inc', 'Federal-gov', 'Local-gov', 'State-gov', 'Without-pay', 'Never-worked']
FNLWFTS = [86500, 15000, 245000, 1484705]
EDUCATION = ['Bachelors', 'Some-college', '11th', 'HS-grad', 'Prof-school', 'Assoc-acdm', 'Assoc-voc', '9th', '7th-8th', '12th', 'Masters', '1st-4th', '10th', 'Doctorate', '5th-6th', 'Preschool']
EDUCATION_NUM = [8, 10, 12, 14, 16]
MARITAL_STATUS = ['Married-civ-spouse', 'Divorced', 'Never-married', 'Separated', 'Widowed', 'Married-spouse-absent', 'Married-AF-spouse']
OCCUPATION = ['Tech-support', 'Craft-repair', 'Other-service', 'Sales', 'Exec-managerial', 'Prof-specialty', 'Handlers-cleaners', 'Machine-op-inspct', 'Adm-clerical', 'Farming-fishing', 'Transport-moving', 'Priv-house-serv', 'Protective-serv', 'Armed-Forces']
RELATIONSHIP = ['Wife', 'Own-child', 'Husband', 'Not-in-family', 'Other-relative', 'Unmarried']
RACE = ['White', 'Asian-Pac-Islander', 'Amer-Indian-Eskimo', 'Other', 'Black']
SEX = ['Female', 'Male']
CAPITAL_GAIN = [6000, 11600, 18000, 99999]
CAPITAL_LOSS = [1500, 2500, 4356]
HOURS_PER_WEEK = [39, 40, 52, 99]
NATIVE_COUNTRY = ['United-States', 'Cambodia', 'England', 'Puerto-Rico', 'Canada', 'Germany', 'Outlying-US(Guam-USVI-etc)', 'India', 'Japan', 'Greece', 'South', 'China', 'Cuba', 'Iran', 'Honduras', 'Philippines', 'Italy', 'Poland', 'Jamaica', 'Vietnam', 'Mexico', 'Portugal', 'Ireland', 'France', 'Dominican-Republic', 'Laos', 'Ecuador', 'Taiwan', 'Haiti', 'Columbia', 'Hungary', 'Guatemala', 'Nicaragua', 'Scotland', 'Thailand', 'Yugoslavia', 'El-Salvador', 'Trinadad&Tobago', 'Peru', 'Hong', 'Holand-Netherlands']
CLASSIFICATION = ['>50K', '<=50K']

def lower_index(l, e):
    i = 0; e = int(e)
    for x in l:
        if e <= x: return i
        i += 1
    print l, e
    raise ValueError

def parse(example):
    """
    parses a training example [attr1, attr2, ...] in a list of
    integers that represents the binary encoding of the example.
    """
    p1 = [0] * 8; p2 = [0] * 7;

    #
    # P1
    #
    p1[0] = 1 << lower_index(AGE, example[0])

    try:
        p1[1] = 1 << WORKCLASS.index(example[1])
    except ValueError:
        p1[1] = 0xff
    p1[1] <<= 4

    p1[2] = 1 << lower_index(FNLWFTS, example[2])
    p1[2] <<= 12

    try:
        p1[3] = 1 << EDUCATION.index(example[3])
    except ValueError:
        p1[3] = 0xffff
    p1[3] <<= 16

    p1[4] = 1 << lower_index(EDUCATION_NUM, example[4])
    p1[4] <<= 32

    try:
        p1[5] = 1 << MARITAL_STATUS.index(example[5])
    except ValueError:
        p1[5] = 0x7f
    p1[5] <<= 37

    try:
        p1[6] = 1 << OCCUPATION.index(example[6])
    except ValueError:
        p1[6] = 0x3fff
    p1[6] <<= 44

    try:
        p1[7] = 1 << RELATIONSHIP.index(example[7])
    except ValueError:
        p1[7] = 0x3f
    p1[7] <<= 58

    #
    # P2
    #
    try:
        p2[0] = 1 << RACE.index(example[8])
    except ValueError:
        p2[0] = 0x1f

    try:
        p2[1] = 1 << SEX.index(example[9])
    except ValueError:
        p2[1] = 0x3
    p2[1] <<= 5

    p2[2] = 1 << lower_index(CAPITAL_GAIN, example[10])
    p2[2] <<= 7

    p2[3] = 1 << lower_index(CAPITAL_LOSS, example[11])
    p2[3] <<= 11

    p2[4] = 1 << lower_index(HOURS_PER_WEEK, example[12])
    p2[4] <<= 14

    try:
        p2[5] = 1 << NATIVE_COUNTRY.index(example[13])
    except ValueError:
        p2[5] = 0x1ffffffffff
    p2[5] <<= 18

    #
    # CLASSIFICATION
    #
    p2[6] = 1 if example[14] == '>50K' else 0
    p2[6] <<= 59

    return (sum(p1), sum(p2))

def main():
    f = open('data/adult.data', 'r')
    parsed = [parse(line.split()) for line in f if '?' not in line.split()]
    f.close()
    f = open('data/adult.bin','wb')
    for p in parsed:
        f.write(struct.pack('@L',p[0]))
        f.write(struct.pack('@L',p[1]))
    f.close()

if __name__ == '__main__':
    main()
