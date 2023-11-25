package me.mrletsplay.shareclientcore.connection;

import me.mrletsplay.shareclientcore.document.Char;

public record Change(int document, ChangeType type, Char character) {}
